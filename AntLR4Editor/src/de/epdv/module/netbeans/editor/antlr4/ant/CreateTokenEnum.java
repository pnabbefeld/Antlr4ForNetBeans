/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.epdv.module.netbeans.editor.antlr4.ant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 *
 * @author peter
 */
public class CreateTokenEnum extends Task {

    private static class Wildcard {

        private final String text;
        private final boolean start;
        private final boolean end;

        public Wildcard(String text, boolean start, boolean end) {
            this.text = text;
            this.start = start;
            this.end = end;
        }

        public boolean test(String string) {
            if (start && end) {
                return string.contains(text);
            } else if (start) {
                return string.endsWith(text);
            } else if (end) {
                return string.startsWith(text);
            } else {
                return string.equals(text);
            }
        }
    }

    private static class WildcardAssignment {

        private final Wildcard wildcard;
        private final String category;

        public WildcardAssignment(Wildcard wildcard, String category) {
            this.wildcard = wildcard;
            this.category = category;
        }

        public Wildcard getWildcard() {
            return wildcard;
        }

        public String getCategory() {
            return category;
        }
    }

    private static class TokenCategoriesDefinition {

        private final Map<String, String> categoriesAssignmentsMap = new TreeMap<>();
        private final List<WildcardAssignment> categoriesAssignmentList = new ArrayList<>();
        private final Map<String, String> blocksMap = new TreeMap<>();
        private final List<Wildcard> blocksList = new ArrayList<>();
        private String defaultCategory;

        public TokenCategoriesDefinition() {
        }

        public String getDefaultCategory() {
            return defaultCategory;
        }

        public void setDefaultCategory(String defaultCategory) {
            this.defaultCategory = defaultCategory;
        }

        public void addCategory(String tokenList, String category) {
            String[] tokenNames = tokenList.split(",");
            for (String tokenName : tokenNames) {
                Wildcard wildcard = getWildcard(tokenName);
                if (wildcard == null) {
                    categoriesAssignmentsMap.put(tokenName, category);
                } else {
                    categoriesAssignmentList.add(new WildcardAssignment(wildcard, category));
                }
            }
        }

        public String getCategory(String tokenName) {
            String category = categoriesAssignmentsMap.get(tokenName);
            if (category == null) {
                for (WildcardAssignment wildcardAssignment : categoriesAssignmentList) {
                    if (wildcardAssignment.getWildcard().test(tokenName)) {
                        category = wildcardAssignment.getCategory();
                        break;
                    }
                }
                if (category == null) {
                    category = defaultCategory;
                }
            }
            return category;
        }

        public void addBlock(String blockLimits) {
            String[] limitNames = blockLimits.split(",");
            if (limitNames.length > 2) {
                throw new IllegalArgumentException("Too many block limits for block");
            } else if (limitNames.length > 1) {
                for (String limitName : limitNames) {
                    if (getWildcard(limitName) != null) {
                        throw new IllegalArgumentException("Wildcards not supported");
                    }
                }
                blocksMap.put(limitNames[0], limitNames[1]);
                blocksMap.put(limitNames[1], limitNames[0]);
            } else {
                Wildcard wildcard = getWildcard(limitNames[0]);
                if (wildcard == null) {
                    throw new IllegalArgumentException("Wildcards expected");
                }
                blocksList.add(wildcard);
            }
        }

        private Wildcard getWildcard(String tokenName) {
            String test = tokenName.trim();
            int p = 0;
            int q = test.length();
            int m = 1;
            if (tokenName.startsWith("*")) {
                p = 1;
                m++;
            }
            boolean end = false;
            if (tokenName.endsWith("*")) {
                end = true;
                q--;
                m++;
            }
            if (test.length() < m) {
                throw new IllegalArgumentException("Token name too short");
            }
            test = test.substring(p, q);
            if (test.contains("*")) {
                throw new IllegalArgumentException("Unsupported wildcard syntax");
            }
            return m > 1 ? new Wildcard(test, p > 0, end) : null;
        }
    }

    private String dirName;
    private String tokensFileName;
    private String definitionsFileName;
    private String pkgName;
    private String pkg;

    public CreateTokenEnum() {
    }

    public void setDir(String dirName) {
        this.dirName = dirName;
    }

    public void setTokens(String tokensFileName) {
        this.tokensFileName = tokensFileName;
    }

    public void setDefs(String definitionsFileName) {
        this.definitionsFileName = definitionsFileName;
    }

    public void setPackage(String pkgName) {
        this.pkgName = pkgName;
    }

    @Override
    public void execute() throws BuildException {
        try {
            File baseDir = super.getProject().getBaseDir();
            File srcDir = new File(baseDir, "src");
            File dir;
            if (dirName == null) {
                dir = srcDir;
            } else {
                File test = new File(dirName);
                if (test.isAbsolute()) {
                    dir = test;
                    if (pkgName == null) {
                        throw new NullPointerException();
                    }
                    pkg = pkgName;
                } else {
                    dir = new File(srcDir, dirName);
                    pkg = pkgName == null ? dirName.replace('/', '.') : pkgName;
                }
            }
            File tokensFile = new File(dir, tokensFileName);
            File defsFile = definitionsFileName == null ? null : new File(dir, definitionsFileName);
            File destFile = new File(dir, "TokenType.java");
            generateTokenTypeEnum(destFile, tokensFile, defsFile);
        } catch (RuntimeException | IOException ex) {
            throw new BuildException(ex);
        }
    }

    private void generateTokenTypeEnum(File destFile, File tokensFile, File defsFile) throws IOException {
        TokenCategoriesDefinition tcDef;
        if (defsFile == null) {
            tcDef = new TokenCategoriesDefinition();
            tcDef.setDefaultCategory("keyword");
            tcDef.addCategory("ID*", "identifier");
            tcDef.addCategory("WS,WHITESPACE", "whitespace");
            tcDef.addBlock("*BRACE*");
        } else {
            tcDef = getTokenCategoriesDefinition(defsFile);
        }
        generateTokenTypeEnum(destFile, tokensFile, tcDef);
    }

    private void generateTokenTypeEnum(File destFile, File tokensFile, TokenCategoriesDefinition tcDef) throws IOException {
        FileOutputStream fos = new FileOutputStream(destFile);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        try (PrintWriter pw = new PrintWriter(osw)) {
            pw.println("package " + pkg + ";");
            pw.println();
            pw.println("public enum TokenType {");
            createEnumConstants(pw, tokensFile, tcDef);
            pw.println("    public int id;");
            pw.println("    public String category;");
            pw.println("    public String text;");
            pw.println();
            pw.println("    private TokenType(int id, String category) {");
            pw.println("        this.id = id;");
            pw.println("        this.category = category;");
            pw.println("    }");
            pw.println();
            pw.println("    public static TokenType valueOf(int id) {");
            pw.println("        TokenType[] values = values();");
            pw.println("        for (TokenType value : values) {");
            pw.println("            if (value.id == id) {");
            pw.println("                return value;");
            pw.println("            }");
            pw.println("        }");
            pw.println("        throw new IllegalArgumentException(\"The id \" + id + \" is not recognized\");");
            pw.println("    }");
            pw.println("}");
            pw.flush();
        }
    }

    private void createEnumConstants(PrintWriter pw, File tokensFile, TokenCategoriesDefinition tcDef) throws IOException {
        FileInputStream fis = new FileInputStream(tokensFile);
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        boolean first = true;
        String line;
        while ((line = br.readLine()) != null) {
            String test = line.trim();
            if (!test.isEmpty()) {
                char ch = test.charAt(0);
                if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z')) {
                    if (first) {
                        first = false;
                    } else {
                        pw.println(',');
                    }
                    // Create enum constant
                    pw.print("    " + createEnumConstant(test, tcDef));
                }
            }
        }
        pw.println(';');
        pw.println();
    }

    private String createEnumConstant(String line, TokenCategoriesDefinition tcDef) {
        String[] tkDef = line.split("=");
        String tkName = tkDef[0];
        int tkId = Integer.parseInt(tkDef[1]);
        String tkCat = tcDef.getCategory(tkName);
        return tkName + '(' + tkId + ", \"" + tkCat + "\")";
    }

    private TokenCategoriesDefinition getTokenCategoriesDefinition(File defsFile) throws IOException {
        TokenCategoriesDefinition tcDef = new TokenCategoriesDefinition();
        FileInputStream fis = new FileInputStream(defsFile);
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            String test = line.trim();
            if (!test.isEmpty()) {
                addTokenCategoriesDefinition(tcDef, line);
            }
        }
        return tcDef;
    }

    private void addTokenCategoriesDefinition(TokenCategoriesDefinition tcDef, String line) {
        if (line.startsWith("default:")) {
            String[] fields = getFields(line, "default:".length());
            tcDef.setDefaultCategory(fields[0]);
        } else if (line.startsWith("category(")) {
            int[] positions = new int[3];
            positions[0] = "category(".length();
            positions[1] = line.indexOf(')', positions[0]);
            positions[2] = line.indexOf(':', positions[1]);
            String[] fields = getFields(line, positions);
            tcDef.addCategory(fields[0], fields[1]);
        } else if (line.startsWith("block:")) {
            String[] fields = getFields(line, "block:".length());
            tcDef.addBlock(fields[0]);
        }
    }

    private String[] getFields(String line, int... positions) {
        int n = (positions.length + 1) / 2;
        String[] res = new String[n];
        int i = 0;
        while (i < n) {
            res[i] = getField(line, positions, i * 2);
            i++;
        }
        return res;
    }

    private String getField(String line, int[] positions, int pos) {
        int p = positions[pos];
        int q = (pos + 1) < positions.length ? positions[pos + 1] : line.length();
        return line.substring(p, q);
    }
}
