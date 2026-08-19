import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.border.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.prefs.Preferences;
import java.util.regex.*;
import java.nio.charset.StandardCharsets;
import com.sun.net.httpserver.*;

public class LumixCode extends JFrame {

    static {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            try { Files.writeString(Path.of("error.log"), sw.toString(), StandardOpenOption.CREATE, StandardOpenOption.APPEND); } catch (IOException ignored) {}
        });
    }

    // ═══════════════════════════════════════════════════════
    // THEME SYSTEM
    // ═══════════════════════════════════════════════════════
    public static class Theme {
        public String name; public boolean isLight;
        public Color BG, BG2, BG3, BG4, BG5, BORDER, BORDER2;
        public Color ACCENT, ACCENT2, ACCENT3, GRAD1, GRAD2;
        public Color TEXT, TEXT2, TEXT3;
        public Color RED, ORANGE, YELLOW, GREEN, CYAN, BLUE, PURPLE, PINK;
        public Color C_KEYWORD, C_STRING, C_COMMENT, C_NUMBER, C_FUNCTION, C_CLASS, C_BRACKET;
        public Color SCROLLBAR, SELECTION, LINE_HIGHLIGHT;
        public Color SUCCESS, WARNING, ERROR, INFO;
        Theme(String n, boolean light, Color bg, Color bg2, Color bg3, Color bg4, Color bg5,
              Color b1, Color b2, Color acc, Color acc2, Color acc3, Color g1, Color g2,
              Color txt, Color txt2, Color txt3,
              Color r, Color o, Color y, Color g, Color c, Color b, Color p, Color pk,
              Color kw, Color str, Color cmt, Color num, Color fn, Color cls, Color brk,
              Color scr, Color sel, Color hl, Color success, Color warning, Color error, Color info) {
            name=n; isLight=light;
            BG=bg; BG2=bg2; BG3=bg3; BG4=bg4; BG5=bg5; BORDER=b1; BORDER2=b2;
            ACCENT=acc; ACCENT2=acc2; ACCENT3=acc3; GRAD1=g1; GRAD2=g2;
            TEXT=txt; TEXT2=txt2; TEXT3=txt3;
            RED=r; ORANGE=o; YELLOW=y; GREEN=g; CYAN=c; BLUE=b; PURPLE=p; PINK=pk;
            C_KEYWORD=kw; C_STRING=str; C_COMMENT=cmt; C_NUMBER=num; C_FUNCTION=fn; C_CLASS=cls; C_BRACKET=brk;
            SCROLLBAR=scr; SELECTION=sel; LINE_HIGHLIGHT=hl;
            SUCCESS=success; WARNING=warning; ERROR=error; INFO=info;
        }
    }

    public static Theme T;
    public static final Map<String, Theme> THEMES = new LinkedHashMap<>();
    public static final String[] FONTS = {"Consolas", "JetBrains Mono", "Fira Code", "Source Code Pro", "Monospaced"};
    public static String CURRENT_FONT = "Consolas";
    public static int FONT_SIZE = 14;

    static {
        THEMES.put("Lumix Ember", new Theme("Lumix Ember", false,
            new Color(0x141110), new Color(0x1A1614), new Color(0x221D1A), new Color(0x0F0C0B), new Color(0x181311),
            new Color(0x382D26), new Color(0x483B32),
            new Color(0xFF5722), new Color(0xFF8A65), new Color(0xE64A19), new Color(0xFF5722), new Color(0xFF7043),
            new Color(0xF5E6D3), new Color(0xD7CCC8), new Color(0x8D7B6E),
            new Color(0xFF5252), new Color(0xFF9800), new Color(0xFFC107), new Color(0x8BC34A),
            new Color(0x00BCD4), new Color(0x03A9F4), new Color(0x9C27B0), new Color(0xE91E63),
            new Color(0xFF9800), new Color(0xFFCC80), new Color(0x795548), new Color(0xFFD54F),
            new Color(0xFFAB91), new Color(0xFFCC80), new Color(0xA1887F),
            new Color(0x3A2E26), new Color(0x4E342E), new Color(0x2D231D),
            new Color(0x8BC34A), new Color(0xFFC107), new Color(0xFF5252), new Color(0x00BCD4)));
        THEMES.put("Lumix Aurora", new Theme("Lumix Aurora", false,
            new Color(0x0A0E27), new Color(0x111835), new Color(0x1A2245), new Color(0x0D1230), new Color(0x151B3D),
            new Color(0x2A3160), new Color(0x3A4280),
            new Color(0x00D9FF), new Color(0x7C3AED), new Color(0xFF00AA), new Color(0x00D9FF), new Color(0x7C3AED),
            new Color(0xE2E8F0), new Color(0xA0AEC0), new Color(0x718096),
            new Color(0xFF6B6B), new Color(0xFFA502), new Color(0xFFD43B), new Color(0x69DB7C),
            new Color(0x00D9FF), new Color(0x4C6EF5), new Color(0xBE4BDB), new Color(0xF783AC),
            new Color(0xFF79C6), new Color(0xA5D6FF), new Color(0x72C4A6), new Color(0xFFD580),
            new Color(0x95E1D3), new Color(0xF38181), new Color(0xAA96DA),
            new Color(0x2A3160), new Color(0x1A2F6F), new Color(0x162050),
            new Color(0x69DB7C), new Color(0xFFA502), new Color(0xFF6B6B), new Color(0x4C6EF5)));
        THEMES.put("Lumix Midnight", new Theme("Lumix Midnight", false,
            new Color(0x0D0D14), new Color(0x12121C), new Color(0x18182A), new Color(0x0A0A10), new Color(0x151520),
            new Color(0x252535), new Color(0x353550),
            new Color(0x7C6AF7), new Color(0x9D8BFF), new Color(0x5A48D8), new Color(0x7C6AF7), new Color(0xC99EFF),
            new Color(0xCDD6F4), new Color(0x9098BB), new Color(0x555577),
            new Color(0xFF6B8A), new Color(0xFFB380), new Color(0xFFD580), new Color(0x78E89A),
            new Color(0x61D8F0), new Color(0x7EB4FF), new Color(0xC99EFF), new Color(0xFF8FA3),
            new Color(0xC99EFF), new Color(0x78E89A), new Color(0x444466), new Color(0xFFB380),
            new Color(0x7EB4FF), new Color(0xFFD580), new Color(0x61D8F0),
            new Color(0x252535), new Color(0x3A2D8A), new Color(0x1A1A2C),
            new Color(0x78E89A), new Color(0xFFD580), new Color(0xFF6B8A), new Color(0x7EB4FF)));
        THEMES.put("Lumix Light", new Theme("Lumix Light", true,
            new Color(0xFAFAFA), new Color(0xF5F5F5), new Color(0xEEEEEE), new Color(0xFFFFFF), new Color(0xF0F0F0),
            new Color(0xE0E0E0), new Color(0xD0D0D0),
            new Color(0x0066CC), new Color(0x0052A3), new Color(0x003D7A), new Color(0x0066CC), new Color(0x0052A3),
            new Color(0x1A1A1A), new Color(0x4A4A4A), new Color(0x6A6A6A),
            new Color(0xD32F2F), new Color(0xF57C00), new Color(0xFBC02D), new Color(0x388E3C),
            new Color(0x0097A7), new Color(0x1976D2), new Color(0x7B1FA2), new Color(0xC2185B),
            new Color(0xD32F2F), new Color(0x388E3C), new Color(0x757575), new Color(0xF57C00),
            new Color(0x1976D2), new Color(0x7B1FA2), new Color(0x0097A7),
            new Color(0xBDBDBD), new Color(0xE3F2FD), new Color(0xFFF8E1),
            new Color(0x388E3C), new Color(0xF57C00), new Color(0xD32F2F), new Color(0x1976D2)));
        THEMES.put("Matrix", new Theme("Matrix", false,
            new Color(0x000000), new Color(0x0A0A0A), new Color(0x0D0D0D), new Color(0x050505), new Color(0x080808),
            new Color(0x003300), new Color(0x004400),
            new Color(0x00FF00), new Color(0x00DD00), new Color(0x00BB00), new Color(0x00FF00), new Color(0x00DD00),
            new Color(0x00FF00), new Color(0x00CC00), new Color(0x009900),
            new Color(0xFF0000), new Color(0xFF8800), new Color(0xFFFF00), new Color(0x00FF00),
            new Color(0x00FFFF), new Color(0x0088FF), new Color(0xFF00FF), new Color(0xFF0088),
            new Color(0x00FF00), new Color(0x00FF00), new Color(0x008800), new Color(0x00FF00),
            new Color(0x00FF00), new Color(0x00FF00), new Color(0x00FF00),
            new Color(0x003300), new Color(0x002200), new Color(0x001100),
            new Color(0x00FF00), new Color(0xFFFF00), new Color(0xFF0000), new Color(0x00FFFF)));
    }

    // ══════════════════════════════════════════════════════
    // AUTOCOMPLETE
    // ═══════════════════════════════════════════════════════
    static class CompletionItem {
        String label, detail, snippet; int type;
        CompletionItem(String l, String d, String s, int t) { label=l; detail=d; snippet=s; type=t; }
    }
    static final Map<String, List<CompletionItem>> COMPLETIONS = new HashMap<>();
    static {
        List<CompletionItem> java = new ArrayList<>();
        for (String k : new String[]{"public","private","protected","static","final","void","class","interface","extends","implements","import","package","return","if","else","for","while","do","switch","case","break","continue","try","catch","finally","throw","throws","new","this","super","null","true","false"})
            java.add(new CompletionItem(k, "keyword", k, 0));
        for (String c : new String[]{"String","Integer","Boolean","List","Map","ArrayList","HashMap","Set","HashSet","Optional","Stream"})
            java.add(new CompletionItem(c, "class", c, 3));
        java.add(new CompletionItem("System.out.println", "method", "System.out.println(", 4));
        java.add(new CompletionItem("main", "method", "public static void main(String[] args) {\n\t\n}", 4));
        java.add(new CompletionItem("toString", "method", "toString()", 4));
        java.add(new CompletionItem("equals", "method", "equals(", 4));
        COMPLETIONS.put("Java", java);

        List<CompletionItem> py = new ArrayList<>();
        for (String k : new String[]{"def","class","if","elif","else","for","while","return","import","from","as","try","except","finally","with","lambda","yield","raise","pass","break","continue","None","True","False","self","async","await"})
            py.add(new CompletionItem(k, "keyword", k, 0));
        for (String f : new String[]{"print","len","range","input","int","str","list","dict","open","map","filter","zip","enumerate","sorted"})
            py.add(new CompletionItem(f, "function", f + "(", 1));
        py.add(new CompletionItem("__main__", "snippet", "if __name__ == \"__main__\":\n\t", 7));
        py.add(new CompletionItem("__init__", "method", "def __init__(self):\n\t", 4));
        COMPLETIONS.put("Python", py);

        List<CompletionItem> js = new ArrayList<>();
        for (String k : new String[]{"function","const","let","var","class","extends","if","else","for","while","return","import","export","default","async","await","try","catch","finally","throw","new","this","null","undefined","true","false"})
            js.add(new CompletionItem(k, "keyword", k, 0));
        js.add(new CompletionItem("console.log", "function", "console.log(", 1));
        js.add(new CompletionItem("console.error", "function", "console.error(", 1));
        js.add(new CompletionItem("document.getElementById", "function", "document.getElementById(", 1));
        js.add(new CompletionItem("addEventListener", "function", "addEventListener(", 1));
        js.add(new CompletionItem("setTimeout", "function", "setTimeout(", 1));
        js.add(new CompletionItem("fetch", "function", "fetch(", 1));
        js.add(new CompletionItem("Promise", "class", "Promise", 3));
        js.add(new CompletionItem("Array", "class", "Array", 3));
        js.add(new CompletionItem("JSON.stringify", "method", "JSON.stringify(", 4));
        js.add(new CompletionItem("JSON.parse", "method", "JSON.parse(", 4));
        COMPLETIONS.put("JavaScript", js);
        COMPLETIONS.put("TypeScript", js);

        List<CompletionItem> cpp = new ArrayList<>();
        for (String k : new String[]{"int","long","double","float","char","bool","auto","const","static","class","struct","namespace","using","include","define","if","else","for","while","do","return","new","delete","nullptr","true","false","void"})
            cpp.add(new CompletionItem(k, "keyword", k, 0));
        cpp.add(new CompletionItem("std::cout", "function", "std::cout << ", 1));
        cpp.add(new CompletionItem("std::cin", "function", "std::cin >> ", 1));
        cpp.add(new CompletionItem("std::vector", "class", "std::vector<", 3));
        cpp.add(new CompletionItem("std::string", "class", "std::string", 3));
        COMPLETIONS.put("C++", cpp);
        COMPLETIONS.put("C", cpp);

        List<CompletionItem> rust = new ArrayList<>();
        for (String k : new String[]{"fn","let","mut","pub","use","mod","struct","enum","impl","trait","if","else","match","for","while","loop","return","self","Self","true","false"})
            rust.add(new CompletionItem(k, "keyword", k, 0));
        rust.add(new CompletionItem("println!", "function", "println!(\"{}\", );", 1));
        rust.add(new CompletionItem("Vec", "class", "Vec<", 3));
        rust.add(new CompletionItem("String", "class", "String", 3));
        COMPLETIONS.put("Rust", rust);

        List<CompletionItem> go = new ArrayList<>();
        for (String k : new String[]{"func","var","const","type","struct","interface","package","import","if","else","for","range","return","switch","case","default","go","defer","select","chan","nil","true","false"})
            go.add(new CompletionItem(k, "keyword", k, 0));
        go.add(new CompletionItem("fmt.Println", "function", "fmt.Println(", 1));
        go.add(new CompletionItem("fmt.Printf", "function", "fmt.Printf(", 1));
        go.add(new CompletionItem("main", "function", "func main() {\n\t\n}", 1));
        COMPLETIONS.put("Go", go);

        List<CompletionItem> kt = new ArrayList<>();
        for (String k : new String[]{"fun","val","var","class","interface","object","when","if","else","for","while","return","import","package","null","true","false","this","super","is","as","in","public","private","protected","internal","open","final","abstract","data","sealed","companion"})
            kt.add(new CompletionItem(k, "keyword", k, 0));
        for (String f : new String[]{"println","print","listOf","mapOf","setOf","mutableListOf"})
            kt.add(new CompletionItem(f, "function", f + "(", 1));
        for (String c : new String[]{"String","Int","Boolean","List","Map"})
            kt.add(new CompletionItem(c, "class", c, 3));
        kt.add(new CompletionItem("main", "method", "fun main() {\n\t\n}", 4));
        COMPLETIONS.put("Kotlin", kt);

        List<CompletionItem> html = new ArrayList<>();
        html.add(new CompletionItem("<div>", "tag", "<div>\n\t\n</div>", 5));
        html.add(new CompletionItem("<span>", "tag", "<span></span>", 5));
        html.add(new CompletionItem("<a>", "tag", "<a href=\"\"></a>", 5));
        html.add(new CompletionItem("<button>", "tag", "<button></button>", 5));
        html.add(new CompletionItem("<input>", "tag", "<input type=\"\" name=\"\">", 5));
        html.add(new CompletionItem("<img>", "tag", "<img src=\"\" alt=\"\">", 5));
        html.add(new CompletionItem("<form>", "tag", "<form>\n\t\n</form>", 5));
        html.add(new CompletionItem("<ul>", "tag", "<ul>\n\t<li></li>\n</ul>", 5));
        html.add(new CompletionItem("<script>", "tag", "<script>\n\t\n</script>", 5));
        html.add(new CompletionItem("<style>", "tag", "<style>\n\t\n</style>", 5));
        html.add(new CompletionItem("<!DOCTYPE html>", "snippet", "<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n<meta charset=\"UTF-8\">\n<title></title>\n</head>\n<body>\n\t\n</body>\n</html>", 7));
        COMPLETIONS.put("HTML/CSS/JS", html);
    }

    // ═══════════════════════════════════════════════════════
    // LANGUAGES
    // ═══════════════════════════════════════════════════════
    static final class Lang {
        final String name; final String[] exts; final String icon; final Color color;
        Lang(String n, String[] e, String i, Color c) { name=n; exts=e; icon=i; color=c; }
        boolean matches(String f) { for (String ex : exts) if (f.endsWith(ex)) return true; return false; }
    }
    static final Map<String, Lang> LANGS = new LinkedHashMap<>();
    static {
        LANGS.put("Java",        new Lang("Java",        new String[]{".java"},             "J",  new Color(255,152,0)));
        LANGS.put("Python",      new Lang("Python",      new String[]{".py",".pyw"},        "Py", new Color(53,114,165)));
        LANGS.put("Kotlin",      new Lang("Kotlin",      new String[]{".kt",".kts"},        "Kt", new Color(160,120,220)));
        LANGS.put("JavaScript",  new Lang("JavaScript",  new String[]{".js",".mjs"},        "JS", new Color(240,210,80)));
        LANGS.put("TypeScript",  new Lang("TypeScript",  new String[]{".ts",".tsx"},        "TS", new Color(49,120,198)));
        LANGS.put("C",           new Lang("C",           new String[]{".c",".h"},           "C",  new Color(0,89,155)));
        LANGS.put("C++",         new Lang("C++",         new String[]{".cpp",".cc",".cxx"}, "C+", new Color(0,89,155)));
        LANGS.put("Rust",        new Lang("Rust",        new String[]{".rs"},               "Rs", new Color(220,100,60)));
        LANGS.put("Go",          new Lang("Go",          new String[]{".go"},               "Go", new Color(0,173,216)));
        LANGS.put("Ruby",        new Lang("Ruby",        new String[]{".rb"},               "Rb", new Color(200,60,60)));
        LANGS.put("PHP",         new Lang("PHP",         new String[]{".php"},              "Ph", new Color(120,130,200)));
        LANGS.put("Bash",        new Lang("Bash",        new String[]{".sh",".bash"},       "Bh", new Color(100,160,100)));
        LANGS.put("Lua",         new Lang("Lua",         new String[]{".lua"},              "Lu", new Color(0,80,180)));
        LANGS.put("HTML/CSS/JS", new Lang("HTML/CSS/JS", new String[]{".html",".css",".htm"},"Ht", new Color(227,76,38)));
    }
    static Lang getLang(String f) {
        for (Lang l : LANGS.values()) if (l.matches(f)) return l;
        return LANGS.get("HTML/CSS/JS");
    }

    static final Map<String, String> TEMPLATES = new LinkedHashMap<>();
    static {
        TEMPLATES.put("Java",        "public class Main {\n    public static void main(String[] args) {\n        System.out.println(\"Hello, Lumix!\");\n    }\n}\n");
        TEMPLATES.put("Python",      "def main():\n    print(\"Hello, Lumix!\")\n\nif __name__ == \"__main__\":\n    main()\n");
        TEMPLATES.put("Kotlin",      "fun main() {\n    println(\"Hello, Lumix!\")\n}\n");
        TEMPLATES.put("JavaScript",  "const greet = (name) => `Hello, ${name}!`;\nconsole.log(greet('Lumix'));\n");
        TEMPLATES.put("C++",         "#include <iostream>\nint main() {\n    std::cout << \"Hello, Lumix!\" << std::endl;\n    return 0;\n}\n");
        TEMPLATES.put("Rust",        "fn main() {\n    println!(\"Hello, Lumix!\");\n}\n");
        TEMPLATES.put("Go",          "package main\nimport \"fmt\"\nfunc main() {\n    fmt.Println(\"Hello, Lumix!\")\n}\n");
        TEMPLATES.put("HTML/CSS/JS", "<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n<meta charset=\"UTF-8\">\n<title>Lumix</title>\n<style>\nbody { font-family: sans-serif; background: #1a1a2e; color: #fff; text-align: center; padding: 50px; }\nh1 { color: #FF5722; }\n</style>\n</head>\n<body>\n<h1>Hello, Lumix!</h1>\n<p>Live Server is running!</p>\n</body>\n</html>\n");
    }

    private static final Map<String, Pattern> PATTERN_CACHE = new HashMap<>();
    private static Pattern cached(String regex) { return PATTERN_CACHE.computeIfAbsent(regex, Pattern::compile); }

    // ═══════════════════════════════════════════════════════
    // ERROR EXPLAINER (для новичков)
    // ═══════════════════════════════════════════════════════
    static class ErrorExplainer {
        private static final Map<Pattern, String> EXPLAINERS = new LinkedHashMap<>();
        static {
            EXPLAINERS.put(Pattern.compile("cannot find symbol", Pattern.CASE_INSENSITIVE), " Символ не найден. Проверь написание переменной/метода и импорты.");
            EXPLAINERS.put(Pattern.compile("unclosed string literal", Pattern.CASE_INSENSITIVE), " Строка не закрыта. Добавь закрывающую кавычку \".");
            EXPLAINERS.put(Pattern.compile("class.*is public.*should be declared", Pattern.CASE_INSENSITIVE), " Имя файла должно совпадать с именем public класса.");
            EXPLAINERS.put(Pattern.compile("incompatible types", Pattern.CASE_INSENSITIVE), " Типы не совпадают. Проверь присваивание или приведи тип.");
            EXPLAINERS.put(Pattern.compile("exception.*must be caught", Pattern.CASE_INSENSITIVE), " Исключение нужно обработать в try-catch или добавить throws.");
            EXPLAINERS.put(Pattern.compile("';' expected", Pattern.CASE_INSENSITIVE), " Пропущена точка с запятой ; в конце строки.");
            EXPLAINERS.put(Pattern.compile("command not found", Pattern.CASE_INSENSITIVE), " Команда не найдена. Установи инструмент или проверь PATH.");
            EXPLAINERS.put(Pattern.compile("no such file or directory", Pattern.CASE_INSENSITIVE), " Файл или папка не найдены. Проверь путь.");
            EXPLAINERS.put(Pattern.compile("IndexError|ArrayIndexOutOfBounds", Pattern.CASE_INSENSITIVE), " Индекс вне диапазона. Проверь длину массива/списка.");
            EXPLAINERS.put(Pattern.compile("NullPointerException", Pattern.CASE_INSENSITIVE), " Попытка использовать null. Проверь инициализацию объекта.");
            EXPLAINERS.put(Pattern.compile("SyntaxError", Pattern.CASE_INSENSITIVE), " Синтаксическая ошибка. Проверь скобки, отступы, двоеточия.");
        }
        public static String explain(String err) {
            if (err == null) return "";
            for (var e : EXPLAINERS.entrySet()) if (e.getKey().matcher(err).find()) return e.getValue();
            return "";
        }
    }

    // ═══════════════════════════════════════════════════════
    // STATE
    // ═══════════════════════════════════════════════════════
    private JTabbedPane tabs;
    private JTextPane terminal;
    private StyledDocument termDoc;
    private JLabel statusLbl, liveLbl, cursorLbl;
    private JComboBox<String> langBox, runModeBox, themeBox, fontBox;
    private final Map<JTextPane, File> editorFiles = new WeakHashMap<>();
    private final Map<JTextPane, Boolean> editorModified = new WeakHashMap<>();
    private Process runProcess;
    private HttpServer liveServer;
    private boolean liveOn = false;
    private javax.swing.Timer hlTimer;
    private JTree fileTree;
    private DefaultTreeModel treeModel;
    private File projectRoot;
    private JTextPane activeEd;
    private CompletionPopup completionPopup;
    private Point dragStart;
    private JButton activeActivityBtn;
    private ExecutorService executorService;
    private final List<String> terminalHistory = new ArrayList<>();
    private int historyIndex = -1;
    private JTextField terminalInput;
    private javax.swing.Timer pulseTimer;
    private float pulsePhase = 0;
    private String runConfig = "Default";
    private String venvPath = null;
    private boolean zenMode = false;

    static final class Cfg {
        private static final Preferences p = Preferences.userNodeForPackage(Cfg.class);
        static boolean isWin() { return System.getProperty("os.name").toLowerCase().contains("win"); }
        static boolean cmdExists(String c) {
            try { return new ProcessBuilder(isWin() ? new String[]{"where", c} : new String[]{"which", c}).redirectErrorStream(true).start().waitFor() == 0; }
            catch (Exception e) { return false; }
        }
        static String findCmd(String... cmds) { for (String c : cmds) if (cmdExists(c)) return c; return cmds[0]; }
        static int getLivePort() { return p.getInt("livePort", 5500); }
        static String getLastDir() { return p.get("lastDir", System.getProperty("user.home")); }
        static void setLastDir(String d) { p.put("lastDir", d); }
        static String getTheme() { return p.get("theme", "Lumix Ember"); }
        static void setTheme(String t) { p.put("theme", t); }
        static String getFont() { return p.get("font", "Consolas"); }
        static void setFont(String f) { p.put("font", f); }
        static int getFontSize() { return p.getInt("fontSize", 14); }
        static void setFontSize(int s) { p.putInt("fontSize", s); }
    }

    public LumixCode() {
        super("LumixCode — Ultimate Edition");
        executorService = Executors.newCachedThreadPool(r -> { Thread t = new Thread(r); t.setDaemon(true); return t; });
        CURRENT_FONT = Cfg.getFont();
        FONT_SIZE = Cfg.getFontSize();
        T = THEMES.getOrDefault(Cfg.getTheme(), THEMES.get("Lumix Ember"));
        initializeComboBoxes();
        setUndecorated(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1600, 1000);
        setMinimumSize(new Dimension(1000, 700));
        setLocationRelativeTo(null);
        setOpacity(0);
        setVisible(true);
        // Fade-in с easing
        javax.swing.Timer opacityTimer = new javax.swing.Timer(16, null);
        final float[] opacity = {0};
        opacityTimer.addActionListener(e -> {
            opacity[0] += 0.04f;
            if (opacity[0] >= 1.0f) { opacity[0] = 1.0f; opacityTimer.stop(); startPulseAnimation(); }
            float eased = opacity[0] * opacity[0] * (3 - 2 * opacity[0]);
            setOpacity(eased);
        });
        opacityTimer.start();
        buildUI();
        setupShortcuts();
        printWelcome();
        new Thread(this::checkTools, "ToolCheck").start();
    }

    private void initializeComboBoxes() {
        langBox = new JComboBox<>(LANGS.keySet().toArray(new String[0]));
        langBox.setSelectedItem("Java");
        langBox.setFont(new Font("Dialog", Font.PLAIN, 12));
        themeBox = new JComboBox<>(THEMES.keySet().toArray(new String[0]));
        themeBox.setSelectedItem(T.name);
        themeBox.setFont(new Font("Dialog", Font.PLAIN, 12));
        fontBox = new JComboBox<>(FONTS);
        fontBox.setSelectedItem(CURRENT_FONT);
        fontBox.setFont(new Font("Dialog", Font.PLAIN, 12));
        runModeBox = new JComboBox<>(new String[]{"Internal", "External"});
        runModeBox.setSelectedIndex(0);
        runModeBox.setFont(new Font("Dialog", Font.PLAIN, 12));
    }

    // ═══════════════════════════════════════════════════════
    // ANIMATIONS
    // ═══════════════════════════════════════════════════════
    private void startPulseAnimation() {
        if (pulseTimer != null) return;
        pulseTimer = new javax.swing.Timer(40, e -> {
            pulsePhase += 0.08f;
            if (pulsePhase > Math.PI * 2) pulsePhase = 0;
            if (activeActivityBtn != null) activeActivityBtn.repaint();
        });
        pulseTimer.start();
    }

    // ═══════════════════════════════════════════════════════
    // UI BUILDERS
    // ═══════════════════════════════════════════════════════
    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 0)) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(0, 0, T.BG4, 0, getHeight(), T.BG3);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        root.setBackground(T.BG);
        root.setBorder(BorderFactory.createLineBorder(T.BORDER, 1));
        root.add(buildTitleBar(), BorderLayout.NORTH);
        JPanel middle = new JPanel(new BorderLayout());
        middle.setOpaque(false);
        middle.add(buildActivityBar(), BorderLayout.WEST);
        JSplitPane main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildSidebar(), buildWorkArea());
        main.setResizeWeight(0.18);
        main.setDividerSize(1);
        main.setBackground(T.BORDER);
        main.setBorder(null);
        middle.add(main, BorderLayout.CENTER);
        root.add(middle, BorderLayout.CENTER);
        root.add(buildStatusBar(), BorderLayout.SOUTH);
        setContentPane(root);
    }

    // ─── TITLE BAR ───
    private JPanel buildTitleBar() {
        JPanel bar = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(0, 0, T.BG4, getWidth(), 0, T.BG3);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(T.BORDER);
                g2.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
                g2.dispose();
            }
        };
        bar.setOpaque(false);
        bar.setPreferredSize(new Dimension(0, 44));
        MouseAdapter drag = new MouseAdapter() {
            public void mousePressed(MouseEvent e) { if (getExtendedState() == MAXIMIZED_BOTH) return; dragStart = e.getPoint(); }
            public void mouseDragged(MouseEvent e) {
                if (dragStart == null) return;
                Point p = getLocation();
                setLocation(p.x + e.getX() - dragStart.x, p.y + e.getY() - dragStart.y);
            }
        };
        bar.addMouseListener(drag); bar.addMouseMotionListener(drag);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        left.setOpaque(false);
        JLabel logo = new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, T.GRAD1, getWidth(), getHeight(), T.GRAD2);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, 26, 24, 6, 6);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Dialog", Font.BOLD, 13));
                g2.drawString("L", 8, 17);
                g2.dispose();
            }
        };
        logo.setPreferredSize(new Dimension(26, 24));
        left.add(logo);
        JLabel name = new JLabel("LumixCode");
        name.setFont(new Font("Dialog", Font.BOLD, 13));
        name.setForeground(T.TEXT);
        left.add(name);
        left.add(Box.createHorizontalStrut(16));
       for (String m : new String[]{"File","Edit","View","Go","Run","Terminal","Help"}) {
    JLabel ml = new JLabel(m) {
        private boolean isHover = false;  // ✅ Поле анонимного класса JLabel
        
        // ✅ Блок инициализации - добавляем MouseListener ВНУТРЬ JLabel
        {
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { 
                    isHover = true; 
                    repaint(); 
                }
                public void mouseExited(MouseEvent e)  { 
                    isHover = false; 
                    repaint(); 
                }
            });
        }
        
        @Override protected void paintComponent(Graphics g) {
            if (isHover) {  // ✅ Теперь isHover доступен
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(T.ACCENT.getRed(), T.ACCENT.getGreen(), T.ACCENT.getBlue(), 30));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                g2.dispose();
            }
            super.paintComponent(g);
        }
    };
    ml.setFont(new Font("Dialog", Font.PLAIN, 12));
    ml.setForeground(T.TEXT2);
    ml.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
    ml.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    ml.addMouseListener(new MouseAdapter() {
        public void mouseEntered(MouseEvent e) { ml.setForeground(T.TEXT); }
        public void mouseExited(MouseEvent e)  { ml.setForeground(T.TEXT2); }
        public void mouseClicked(MouseEvent e) { handleMenu(m); }
    });
    left.add(ml);
}
        bar.add(left, BorderLayout.WEST);

        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        center.setOpaque(false);
        JTextField cmdPalette = new JTextField("Ctrl+Shift+P — Command Palette") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(T.BG.getRed(), T.BG.getGreen(), T.BG.getBlue(), 180));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(T.BORDER);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        cmdPalette.setFont(new Font("Dialog", Font.PLAIN, 12));
        cmdPalette.setForeground(T.TEXT3);
        cmdPalette.setBackground(new Color(0,0,0,0));
        cmdPalette.setOpaque(false);
        cmdPalette.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        cmdPalette.setPreferredSize(new Dimension(340, 28));
        cmdPalette.setHorizontalAlignment(SwingConstants.CENTER);
        cmdPalette.setEditable(false);
        cmdPalette.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cmdPalette.addMouseListener(new MouseAdapter() { public void mouseClicked(MouseEvent e) { showCommandPalette(); } });
        center.add(cmdPalette);
        bar.add(center, BorderLayout.CENTER);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(winBtn("-", false, e -> setState(ICONIFIED)));
        right.add(winBtn("□", false, e -> {
            if (getExtendedState() == MAXIMIZED_BOTH) setExtendedState(NORMAL);
            else setExtendedState(MAXIMIZED_BOTH);
        }));
        right.add(winBtn("X", true, e -> {
            int r = JOptionPane.showConfirmDialog(this, "Exit LumixCode?", "Exit", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) { executorService.shutdown(); System.exit(0); }
        }));
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private JButton winBtn(String icon, boolean danger, ActionListener a) {
        JButton b = new JButton(icon) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(danger ? T.RED : new Color(T.ACCENT.getRed(), T.ACCENT.getGreen(), T.ACCENT.getBlue(), 40));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                    setForeground(danger ? Color.WHITE : T.TEXT);
                } else setForeground(T.TEXT2);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(new Font("Dialog", Font.PLAIN, 14));
        b.setBackground(null); b.setContentAreaFilled(false); b.setBorderPainted(false); b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(46, 44));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(a);
        return b;
    }

    private void handleMenu(String m) {
        switch (m) {
            case "File" -> {
                String[] opts = {"New File", "Open File", "Open Folder", "Save", "Save As", "Close Tab", "Exit"};
                int r = JOptionPane.showOptionDialog(this, "Choose:", "File", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opts, opts[0]);
                if (r == 0) newTab();
                else if (r == 1) openFile();
                else if (r == 2) openProject();
                else if (r == 3) saveFile();
                else if (r == 4) saveFileAs();
                else if (r == 5) closeCurrentTab();
                else if (r == 6) { executorService.shutdown(); System.exit(0); }
            }
            case "Edit" -> {
                String[] opts = {"Find (Ctrl+F)", "Replace", "Go to Line", "Quick Open (Ctrl+P)"};
                int r = JOptionPane.showOptionDialog(this, "Choose:", "Edit", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opts, opts[0]);
                if (r == 0) showFind();
                else if (r == 1) showReplace();
                else if (r == 2) showGotoLine();
                else if (r == 3) showQuickOpen();
            }
            case "View" -> {
                String[] opts = {"Change Theme", "Change Font", "Zoom In", "Zoom Out", "Toggle Zen Mode"};
                int r = JOptionPane.showOptionDialog(this, "Choose:", "View", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opts, opts[0]);
                if (r == 0) cycleTheme();
                else if (r == 1) showFontDialog();
                else if (r == 2 && FONT_SIZE < 24) { FONT_SIZE++; Cfg.setFontSize(FONT_SIZE); updateAllFonts(); }
                else if (r == 3 && FONT_SIZE > 10) { FONT_SIZE--; Cfg.setFontSize(FONT_SIZE); updateAllFonts(); }
                else if (r == 4) toggleZenMode();
            }
            case "Run" -> runCode();
            case "Tools" -> {
                String[] opts = {"Start Live Server", "Change Language", "Select Python venv"};
                int r = JOptionPane.showOptionDialog(this, "Choose:", "Tools", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opts, opts[0]);
                if (r == 0) toggleLive();
                else if (r == 1) showLanguageDialog();
                else if (r == 2) selectVenv();
            }
            case "Help" -> {
                JOptionPane.showMessageDialog(this,
                    "LumixCode — Ultimate Edition\n\n" +
                    "Ctrl+Enter: Run | Ctrl+S: Save | Ctrl+P: Quick Open\n" +
                    "Ctrl+Space: Autocomplete | Ctrl+Shift+P: Command Palette\n" +
                    "Ctrl+F: Find | Ctrl+G: Go to Line\n" +
                    "Ctrl+Z: Zen Mode | F1: Help\n\n" +
                    "Languages: Java, Python, Kotlin, JS/TS, C/C++, Rust, Go, HTML\n" +
                    "Live Server for HTML/CSS/JS on port 5500\n" +
                    "Terminal: Up/Down history, Tab autocomplete paths",
                    "About LumixCode", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void showLanguageDialog() {
        String[] langs = LANGS.keySet().toArray(new String[0]);
        int r = JOptionPane.showOptionDialog(this, "Select language:", "Language", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, langs, langs[0]);
        if (r >= 0 && langBox != null) langBox.setSelectedIndex(r);
    }

    private void selectVenv() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setDialogTitle("Select Python venv folder");
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            if (new File(f, "bin/python").exists() || new File(f, "Scripts/python.exe").exists()) {
                venvPath = f.getAbsolutePath();
                tprint(" Python venv: " + venvPath + "\n", T.GREEN);
            } else tprint(" Not a valid venv\n", T.RED);
        }
    }

    // ═══════════════════════════════════════════════════════
    // ACTIVITY BAR (с векторными иконками и анимациями)
    // ═══════════════════════════════════════════════════════
    private JPanel buildActivityBar() {
        JPanel bar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(0, 0, T.BG4, 0, getHeight(), T.BG3);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(T.BORDER);
                g2.drawLine(getWidth()-1, 0, getWidth()-1, getHeight());
                g2.dispose();
            }
        };
        bar.setLayout(new BoxLayout(bar, BoxLayout.Y_AXIS));
        bar.setOpaque(false);
        bar.setPreferredSize(new Dimension(56, 0));
        String[][] items = {{"explorer","Explorer"},{"search","Search"},{"git","Git"},{"debug","Debug"},{"settings","Settings"}};
        for (String[] it : items) { bar.add(Box.createVerticalStrut(6)); bar.add(activityBtn(it[0], it[1])); }
        bar.add(Box.createVerticalGlue());
        bar.add(Box.createVerticalStrut(10));
        return bar;
    }

    private JButton activityBtn(String iconType, String tooltip) {
        JButton b = new JButton() {
            private float hoverAnim = 0;
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                boolean isActive = (activeActivityBtn == this);
                boolean isHover = getModel().isRollover();
                float targetHover = isHover ? 1.0f : 0.0f;
                hoverAnim += (targetHover - hoverAnim) * 0.2f;
                if (isActive) {
                    float pulse = (float)(Math.sin(pulsePhase) * 0.5 + 0.5);
                    Color baseColor = new Color(T.ACCENT.getRed(), T.ACCENT.getGreen(), T.ACCENT.getBlue(), (int)(40 + pulse * 30));
                    GradientPaint gp = new GradientPaint(0, 0, baseColor, w, h, new Color(T.ACCENT2.getRed(), T.ACCENT2.getGreen(), T.ACCENT2.getBlue(), (int)(20 + pulse * 20)));
                    g2.setPaint(gp);
                    g2.fillRoundRect(8, 6, w-16, h-12, 10, 10);
                    g2.setColor(T.ACCENT);
                    g2.fillRoundRect(0, h/2 - 12, 3, 24, 2, 2);
                } else if (hoverAnim > 0.01f) {
                    g2.setColor(new Color(T.ACCENT.getRed(), T.ACCENT.getGreen(), T.ACCENT.getBlue(), (int)(hoverAnim * 35)));
                    g2.fillRoundRect(8, 6, w-16, h-12, 10, 10);
                }
                drawVectorIcon(g2, iconType, w/2, h/2 - 2, isActive || isHover ? T.TEXT : T.TEXT3, hoverAnim);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setBackground(null); b.setContentAreaFilled(false); b.setBorderPainted(false); b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(56, 52));
        b.setMaximumSize(new Dimension(56, 52));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setToolTipText(tooltip);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(e -> {
            activeActivityBtn = b;
            b.getParent().repaint();
            switch (tooltip) {
                case "Explorer" -> tprint(" Explorer panel\n", T.INFO);
                case "Search" -> showFind();
                case "Git" -> tprint(" Git panel (coming soon)\n", T.INFO);
                case "Debug" -> tprint(" Debug panel (coming soon)\n", T.INFO);
                case "Settings" -> showSettingsPanel();
            }
        });
        return b;
    }

    private void drawVectorIcon(Graphics2D g2, String type, int cx, int cy, Color color, float scale) {
        g2.setColor(color);
        g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        float s = 1.0f + scale * 0.15f;
        g2.translate(cx, cy);
        g2.scale(s, s);
        g2.translate(-cx, -cy);
        switch (type) {
            case "explorer" -> { g2.fillRoundRect(cx - 10, cy - 6, 20, 14, 2, 2); g2.fillRoundRect(cx - 10, cy - 9, 10, 4, 1, 1); }
            case "search" -> { g2.drawOval(cx - 7, cy - 9, 12, 12); g2.setStroke(new BasicStroke(2.5f)); g2.drawLine(cx + 2, cy + 1, cx + 8, cy + 7); }
            case "git" -> { g2.fillOval(cx - 9, cy - 9, 6, 6); g2.fillOval(cx - 9, cy + 3, 6, 6); g2.fillOval(cx + 3, cy - 9, 6, 6); g2.setStroke(new BasicStroke(1.5f)); g2.drawLine(cx - 6, cy - 3, cx - 6, cy + 3); g2.drawArc(cx - 6, cy - 9, 12, 12, 0, -90); }
            case "debug" -> { g2.fillOval(cx - 6, cy - 8, 12, 14); g2.fillOval(cx - 4, cy - 12, 8, 4); g2.drawLine(cx - 3, cy - 12, cx - 6, cy - 15); g2.drawLine(cx + 3, cy - 12, cx + 6, cy - 15); g2.drawLine(cx - 6, cy - 4, cx - 11, cy - 6); g2.drawLine(cx + 6, cy - 4, cx + 11, cy - 6); g2.drawLine(cx - 6, cy + 2, cx - 11, cy + 2); g2.drawLine(cx + 6, cy + 2, cx + 11, cy + 2); }
            case "settings" -> {
                int teeth = 8, outer = 10, inner = 7;
                for (int i = 0; i < teeth; i++) {
                    double a1 = (i * 2 * Math.PI) / teeth, a2 = ((i + 0.4) * 2 * Math.PI) / teeth, a3 = ((i + 0.6) * 2 * Math.PI) / teeth, a4 = ((i + 1.0) * 2 * Math.PI) / teeth;
                    int[] xs = {cx + (int)(Math.cos(a1) * outer), cx + (int)(Math.cos(a2) * outer), cx + (int)(Math.cos(a3) * inner), cx + (int)(Math.cos(a4) * inner)};
                    int[] ys = {cy + (int)(Math.sin(a1) * outer), cy + (int)(Math.sin(a2) * outer), cy + (int)(Math.sin(a3) * inner), cy + (int)(Math.sin(a4) * inner)};
                    g2.fillPolygon(xs, ys, 4);
                }
                g2.setColor(T.BG3);
                g2.fillOval(cx - 3, cy - 3, 6, 6);
            }
        }
    }

    private void showSettingsPanel() {
        JDialog dlg = new JDialog(this, "Settings", false);
        dlg.setUndecorated(true);
        dlg.setBackground(new Color(0,0,0,0));
        JPanel content = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(T.BG3.getRed(), T.BG3.getGreen(), T.BG3.getBlue(), 240));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(new Color(T.ACCENT.getRed(), T.ACCENT.getGreen(), T.ACCENT.getBlue(), 100));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                g2.dispose();
            }
        };
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("Settings");
        title.setFont(new Font("Dialog", Font.BOLD, 16));
        title.setForeground(T.TEXT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(title);
        content.add(Box.createVerticalStrut(12));

        content.add(sectionLabel("Theme"));
        JPanel themeRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        themeRow.setOpaque(false);
        themeRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (String t : THEMES.keySet()) {
            JButton tb = settingsChip(t, t.equals(T.name));
            tb.addActionListener(e -> { applyNewTheme(t); dlg.dispose(); });
            themeRow.add(tb);
        }
        content.add(themeRow);
        content.add(Box.createVerticalStrut(10));

        content.add(sectionLabel("Font"));
        JPanel fontRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        fontRow.setOpaque(false);
        fontRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (String f : FONTS) {
            JButton fb = settingsChip(f, f.equals(CURRENT_FONT));
            fb.addActionListener(e -> { CURRENT_FONT = f; Cfg.setFont(f); updateAllFonts(); dlg.dispose(); });
            fontRow.add(fb);
        }
        content.add(fontRow);
        content.add(Box.createVerticalStrut(10));

        content.add(sectionLabel("Font Size: " + FONT_SIZE));
        JPanel sizeRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        sizeRow.setOpaque(false);
        sizeRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton minus = settingsChip("A-", false);
        minus.addActionListener(e -> { if (FONT_SIZE > 10) { FONT_SIZE--; Cfg.setFontSize(FONT_SIZE); updateAllFonts(); dlg.dispose(); } });
        JButton plus = settingsChip("A+", false);
        plus.addActionListener(e -> { if (FONT_SIZE < 24) { FONT_SIZE++; Cfg.setFontSize(FONT_SIZE); updateAllFonts(); dlg.dispose(); } });
        sizeRow.add(minus); sizeRow.add(plus);
        content.add(sizeRow);
        content.add(Box.createVerticalStrut(12));

        JButton closeBtn = new JButton("Close") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? T.ACCENT : T.BG4);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        closeBtn.setFont(new Font("Dialog", Font.BOLD, 12));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setBackground(null); closeBtn.setBorderPainted(false); closeBtn.setFocusPainted(false);
        closeBtn.setAlignmentX(Component.RIGHT_ALIGNMENT);
        closeBtn.addActionListener(e -> dlg.dispose());
        content.add(closeBtn);

        dlg.setContentPane(content);
        dlg.pack();
        dlg.setSize(520, 320);
        Point loc = getLocation();
        dlg.setLocation(loc.x + getWidth() - 540, loc.y + 50);
        dlg.setVisible(true);
        dlg.setOpacity(0);
        javax.swing.Timer t = new javax.swing.Timer(16, null);
        final float[] op = {0};
        t.addActionListener(e -> {
            op[0] += 0.08f;
            if (op[0] >= 1.0f) { op[0] = 1.0f; t.stop(); }
            dlg.setOpacity(op[0]);
        });
        t.start();
    }

    private JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Dialog", Font.BOLD, 11));
        l.setForeground(T.ACCENT);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JButton settingsChip(String text, boolean active) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (active || getModel().isRollover()) {
                    g2.setColor(T.ACCENT);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    setForeground(Color.WHITE);
                } else {
                    g2.setColor(T.BG4);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2.setColor(T.BORDER);
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                    setForeground(T.TEXT2);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(new Font("Dialog", Font.PLAIN, 11));
        b.setBackground(null); b.setBorderPainted(false); b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        return b;
    }

    // ═══════════════════════════════════════════════════════
    // SIDEBAR
    // ═══════════════════════════════════════════════════════
    private JPanel buildSidebar() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(T.BG2);
        panel.setPreferredSize(new Dimension(280, 0));
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, T.BORDER));
        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setBackground(T.BG2);
        hdr.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 12));
        JLabel lbl = new JLabel("EXPLORER");
        lbl.setFont(new Font("Dialog", Font.BOLD, 11));
        lbl.setForeground(T.ACCENT);
        hdr.add(lbl, BorderLayout.WEST);
        panel.add(hdr, BorderLayout.NORTH);
        fileTree = new JTree(new DefaultTreeModel(new DefaultMutableTreeNode("No folder opened")));
        fileTree.setBackground(T.BG2);
        fileTree.setForeground(T.TEXT2);
        fileTree.setFont(new Font("Dialog", Font.PLAIN, 13));
        fileTree.setRowHeight(26);
        fileTree.setCellRenderer(new LumixTreeRenderer());
        fileTree.setShowsRootHandles(true);
        fileTree.addMouseListener(new TreeCtxListener());
        fileTree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();
            if (node != null && node.getUserObject() instanceof File f && f.isFile()) openFileInEditor(f);
        });
        JScrollPane tsp = new JScrollPane(fileTree);
        tsp.setBorder(null); tsp.getViewport().setBackground(T.BG2);
        tsp.getVerticalScrollBar().setUI(new ThinScrollBarUI());
        panel.add(tsp, BorderLayout.CENTER);
        return panel;
    }

    static class ThinScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        @Override protected void configureScrollBarColors() { thumbColor = new Color(T.SCROLLBAR.getRed(), T.SCROLLBAR.getGreen(), T.SCROLLBAR.getBlue(), 180); trackColor = new Color(0,0,0,0); }
        @Override protected JButton createDecreaseButton(int o) { JButton b = new JButton(); b.setPreferredSize(new Dimension(0,0)); return b; }
        @Override protected JButton createIncreaseButton(int o) { JButton b = new JButton(); b.setPreferredSize(new Dimension(0,0)); return b; }
        @Override protected void paintThumb(Graphics g, JComponent c, Rectangle r) { Graphics2D g2 = (Graphics2D) g.create(); g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); g2.setColor(new Color(T.SCROLLBAR.getRed(), T.SCROLLBAR.getGreen(), T.SCROLLBAR.getBlue(), 180)); g2.fillRoundRect(r.x+2, r.y, r.width-4, r.height, 6, 6); g2.dispose(); }
        @Override protected void paintTrack(Graphics g, JComponent c, Rectangle r) { g.setColor(new Color(0,0,0,0)); }
    }

    class LumixTreeRenderer extends DefaultTreeCellRenderer {
        @Override public Component getTreeCellRendererComponent(JTree tree, Object val, boolean sel, boolean exp, boolean leaf, int row, boolean focus) {
            super.getTreeCellRendererComponent(tree, val, sel, exp, leaf, row, focus);
            setOpaque(true);
            setBackgroundSelectionColor(new Color(T.ACCENT.getRed(), T.ACCENT.getGreen(), T.ACCENT.getBlue(), 50));
            setBackgroundNonSelectionColor(T.BG2);
            setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 6));
            setFont(new Font("Dialog", Font.PLAIN, 13));
            if (val instanceof DefaultMutableTreeNode node) {
                Object uo = node.getUserObject();
                if (uo instanceof File f) {
                    if (f.isDirectory()) { setIcon(createFolderIcon(exp)); setForeground(sel ? T.TEXT : T.TEXT2); }
                    else { setIcon(createFileIcon(f.getName())); setForeground(T.TEXT); }
                    setText(f.getName());
                }
            }
            setBackground(sel ? new Color(T.ACCENT.getRed(), T.ACCENT.getGreen(), T.ACCENT.getBlue(), 50) : new Color(0,0,0,0));
            return this;
        }
        private ImageIcon createFolderIcon(boolean open) {
            BufferedImage img = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color c = open ? T.ACCENT : T.ORANGE;
            g2.setPaint(new GradientPaint(0, 0, c, 20, 20, new Color(c.getRed(), c.getGreen(), c.getBlue(), 180)));
            g2.fillRoundRect(1, 5, 17, 12, 3, 3);
            g2.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 220));
            g2.fillRoundRect(3, 3, 8, 4, 2, 2);
            g2.dispose();
            return new ImageIcon(img);
        }
        private ImageIcon createFileIcon(String name) {
            BufferedImage img = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            Lang lang = getLang(name);
            Color c = lang.color;
            g2.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 45));
            g2.fillRoundRect(3, 2, 14, 16, 3, 3);
            g2.setColor(c);
            g2.setFont(new Font("Dialog", Font.BOLD, 9));
            FontMetrics fm = g2.getFontMetrics();
            int w = fm.stringWidth(lang.icon);
            g2.drawString(lang.icon, 10 - w/2, 14);
            g2.dispose();
            return new ImageIcon(img);
        }
    }

    class TreeCtxListener extends MouseAdapter {
        public void mousePressed(MouseEvent e)  { check(e); }
        public void mouseReleased(MouseEvent e) { check(e); }
        private void check(MouseEvent e) {
            if (!e.isPopupTrigger() || projectRoot==null) return;
            int row = fileTree.getRowForLocation(e.getX(), e.getY());
            if (row != -1) fileTree.setSelectionRow(row);
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();
            if (node==null || !(node.getUserObject() instanceof File f)) return;
            showTreeCtx(f, e.getX(), e.getY());
        }
    }

    private void showTreeCtx(File f, int x, int y) {
        JPopupMenu pm = new JPopupMenu();
        pm.setBackground(T.BG3);
        pm.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(T.ACCENT, 1), BorderFactory.createEmptyBorder(4, 4, 4, 4)));
        addCtxItem(pm, "New File",   () -> createNewFile(f.isDirectory() ? f : f.getParentFile()));
        addCtxItem(pm, "New Folder", () -> createNewFolder(f.isDirectory() ? f : f.getParentFile()));
        pm.addSeparator();
        addCtxItem(pm, "Rename", () -> renameFile(f));
        addCtxItem(pm, "Delete", () -> deleteFile(f));
        pm.show(fileTree, x, y);
    }
    private JMenuItem ctxItem(String t) {
        JMenuItem m = new JMenuItem(t);
        m.setBackground(T.BG3); m.setForeground(T.TEXT);
        m.setFont(new Font("Dialog", Font.PLAIN, 12));
        m.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));
        m.setOpaque(true);
        m.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { m.setBackground(new Color(T.ACCENT.getRed(), T.ACCENT.getGreen(), T.ACCENT.getBlue(), 60)); }
            public void mouseExited(MouseEvent e) { m.setBackground(T.BG3); }
        });
        return m;
    }
    private void addCtxItem(JPopupMenu pm, String t, Runnable r) { JMenuItem m = ctxItem(t); m.addActionListener(e -> r.run()); pm.add(m); }

    // ═══════════════════════════════════════════════════════
    // WORK AREA
    // ═══════════════════════════════════════════════════════
    private JSplitPane buildWorkArea() {
        tabs = new JTabbedPane(JTabbedPane.TOP) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(0, 0, T.BG, 0, getHeight(), T.BG2);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        tabs.setBackground(T.BG2);
        tabs.setForeground(T.TEXT2);
        tabs.setFont(new Font("Dialog", Font.PLAIN, 12));
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
        tabs.addChangeListener(e -> {
            int i = tabs.getSelectedIndex();
            if (i < 0) return;
            Component c = tabs.getComponentAt(i);
            if (c instanceof JScrollPane sp && sp.getViewport().getView() instanceof JTextPane tp) {
                activeEd = tp;
                updateLangBox(tp);
            }
        });
        addNewTab("welcome.java", TEMPLATES.get("Java"), null);
        JSplitPane vSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tabs, buildTermPanel());
        vSplit.setResizeWeight(0.72);
        vSplit.setDividerSize(3);
        vSplit.setBackground(T.BORDER);
        vSplit.setBorder(null);
        return vSplit;
    }

    private void updateLangBox(JTextPane ed) {
        File f = editorFiles.get(ed);
        if (f != null) {
            Lang l = getLang(f.getName());
            if (l != null && langBox != null) langBox.setSelectedItem(l.name);
        }
    }

    private void addNewTab(String name, String content, File sourceFile) {
        JTextPane ed = new JTextPane() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(T.BG);
                g2.fillRect(0, 0, getWidth(), getHeight());
                try {
                    Rectangle r = modelToView2D(getCaretPosition()).getBounds();
                    g2.setColor(T.LINE_HIGHLIGHT);
                    g2.fillRect(0, r.y, getWidth(), r.height);
                } catch (Exception ignored) {}
                g2.dispose();
                super.paintComponent(g);
            }
        };
        ed.setBackground(T.BG);
        ed.setForeground(T.TEXT);
        ed.setCaretColor(T.ACCENT);
        ed.setSelectionColor(new Color(T.ACCENT.getRed(), T.ACCENT.getGreen(), T.ACCENT.getBlue(), 60));
        ed.setFont(new Font(CURRENT_FONT, Font.PLAIN, FONT_SIZE));
        ed.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        ed.setText(content);
        ed.setCaretPosition(0);
        if (sourceFile != null) editorFiles.put(ed, sourceFile);
        editorModified.put(ed, false);
        ed.getInputMap().put(KeyStroke.getKeyStroke("TAB"), new AbstractAction() { public void actionPerformed(ActionEvent e) { try { ed.getDocument().insertString(ed.getCaretPosition(), "    ", null); } catch (BadLocationException ignored) {} } });
        ed.getInputMap().put(KeyStroke.getKeyStroke("control SPACE"), "autocomplete");
        ed.getActionMap().put("autocomplete", new AbstractAction() { public void actionPerformed(ActionEvent e) { showAutocomplete(ed); } });
        ed.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                scheduleHl(ed); updateCursor(ed); editorModified.put(ed, true); updateTabTitle(ed);
                try {
                    String inserted = ed.getDocument().getText(e.getOffset(), e.getLength());
                    if (inserted.length() == 1 && Character.isJavaIdentifierPart(inserted.charAt(0))) SwingUtilities.invokeLater(() -> showAutocomplete(ed));
                    else if (completionPopup != null && completionPopup.isVisible()) completionPopup.hideCompletion();
                } catch (Exception ex) {}
            }
            public void removeUpdate(DocumentEvent e) { scheduleHl(ed); updateCursor(ed); editorModified.put(ed, true); updateTabTitle(ed); }
            public void changedUpdate(DocumentEvent e) {}
        });
        ed.addCaretListener(e -> updateCursor(ed));
        LineGutter gutter = new LineGutter(ed);
        JScrollPane sp = new JScrollPane(ed);
        sp.setRowHeaderView(gutter);
        sp.setBorder(null);
        sp.getViewport().setBackground(T.BG);
        sp.getVerticalScrollBar().setUI(new ThinScrollBarUI());
        sp.getHorizontalScrollBar().setUI(new ThinScrollBarUI());
        int idx = tabs.getTabCount();
        tabs.addTab(name, sp);
        tabs.setTabComponentAt(idx, makeTabComp(name, idx, ed));
        tabs.setSelectedIndex(idx);
        activeEd = ed;
    }

    private JPanel makeTabComp(String name, int idx, JTextPane ed) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 2)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (tabs.getSelectedComponent() == ((JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, ed))) {
                    g2.setColor(new Color(T.ACCENT.getRed(), T.ACCENT.getGreen(), T.ACCENT.getBlue(), 30));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(T.ACCENT);
                    g2.fillRect(0, getHeight()-2, getWidth(), 2);
                }
                g2.dispose();
            }
        };
        p.setOpaque(false); p.setBorder(null);
        JLabel lbl = new JLabel(name);
        lbl.setFont(new Font("Dialog", Font.PLAIN, 12));
        lbl.setForeground(T.TEXT2);
        p.add(lbl);
        JButton x = new JButton("x") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) { g2.setColor(T.RED); g2.fillOval(0, 0, getWidth(), getHeight()); setForeground(Color.WHITE); }
                else setForeground(T.TEXT3);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        x.setFont(new Font("Dialog", Font.BOLD, 12));
        x.setBackground(null); x.setContentAreaFilled(false); x.setBorderPainted(false); x.setFocusPainted(false);
        x.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        x.setPreferredSize(new Dimension(18, 18));
        x.addActionListener(e -> {
            for (int i = 0; i < tabs.getTabCount(); i++) {
                Component comp = tabs.getComponentAt(i);
                if (comp instanceof JScrollPane sp && sp.getViewport().getView() == ed) { editorFiles.remove(ed); editorModified.remove(ed); tabs.remove(i); break; }
            }
        });
        p.add(x);
        return p;
    }

    private void updateTabTitle(JTextPane ed) {
        for (int i = 0; i < tabs.getTabCount(); i++) {
            Component comp = tabs.getComponentAt(i);
            if (comp instanceof JScrollPane sp && sp.getViewport().getView() == ed) {
                Boolean mod = editorModified.get(ed);
                String title = tabs.getTitleAt(i);
                if (mod != null && mod && !title.startsWith("*")) tabs.setTitleAt(i, "* " + title);
                break;
            }
        }
    }

    private void newTab() { addNewTab("untitled.java", TEMPLATES.get("Java"), null); }
    private void closeCurrentTab() {
        int i = tabs.getSelectedIndex();
        if (i >= 0) {
            Component comp = tabs.getComponentAt(i);
            if (comp instanceof JScrollPane sp && sp.getViewport().getView() instanceof JTextPane tp) { editorFiles.remove(tp); editorModified.remove(tp); }
            tabs.remove(i);
        }
    }

    // ══════════════════════════════════════════════════════
    // AUTOCOMPLETE
    // ═══════════════════════════════════════════════════════
    class CompletionPopup extends JWindow {
        private JList<CompletionItem> list;
        private DefaultListModel<CompletionItem> model;
        private JTextPane editor;
        private JLabel detailLabel;
        CompletionPopup(JFrame parent) {
            super(parent);
            setBackground(new Color(0,0,0,0));
            JPanel content = new JPanel(new BorderLayout()) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(T.BG3.getRed(), T.BG3.getGreen(), T.BG3.getBlue(), 240));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.setColor(new Color(T.ACCENT.getRed(), T.ACCENT.getGreen(), T.ACCENT.getBlue(), 100));
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                    g2.dispose();
                }
            };
            content.setOpaque(false);
            content.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            model = new DefaultListModel<>();
            list = new JList<>(model);
            list.setBackground(new Color(0,0,0,0));
            list.setForeground(T.TEXT);
            list.setFont(new Font(CURRENT_FONT, Font.PLAIN, 13));
            list.setSelectionBackground(new Color(T.ACCENT.getRed(), T.ACCENT.getGreen(), T.ACCENT.getBlue(), 70));
            list.setSelectionForeground(T.TEXT);
            list.setCellRenderer(new CompletionRenderer());
            list.setFocusable(false);
            list.addMouseListener(new MouseAdapter() { public void mouseClicked(MouseEvent e) { if (e.getClickCount() == 2) insertSelected(); } });
            JScrollPane sp = new JScrollPane(list);
            sp.setBorder(null); sp.getViewport().setBackground(new Color(0,0,0,0));
            sp.getVerticalScrollBar().setUI(new ThinScrollBarUI());
            sp.setPreferredSize(new Dimension(320, 200));
            content.add(sp, BorderLayout.CENTER);
            detailLabel = new JLabel(" ");
            detailLabel.setFont(new Font("Dialog", Font.PLAIN, 11));
            detailLabel.setForeground(T.TEXT3);
            detailLabel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, T.BORDER), BorderFactory.createEmptyBorder(4, 8, 4, 8)));
            detailLabel.setBackground(T.BG4);
            detailLabel.setOpaque(true);
            content.add(detailLabel, BorderLayout.SOUTH);
            list.addListSelectionListener(e -> { CompletionItem sel = list.getSelectedValue(); if (sel != null) detailLabel.setText(sel.detail); });
            setContentPane(content);
            setSize(340, 240);
        }
        class CompletionRenderer extends JLabel implements ListCellRenderer<CompletionItem> {
            CompletionRenderer() { setOpaque(true); setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8)); }
            public Component getListCellRendererComponent(JList<? extends CompletionItem> list, CompletionItem item, int index, boolean isSelected, boolean cellHasFocus) {
                String icon = switch(item.type) { case 0 -> "K"; case 1 -> "f"; case 3 -> "C"; case 4 -> "M"; case 5 -> "T"; case 7 -> "S"; default -> "."; };
                Color iconColor = switch(item.type) { case 0 -> T.C_KEYWORD; case 1 -> T.C_FUNCTION; case 3 -> T.C_CLASS; case 4 -> T.PURPLE; case 5 -> T.YELLOW; case 7 -> T.GREEN; default -> T.TEXT; };
                setText("<html><font color='" + colorToHex(iconColor) + "'><b>" + icon + "</b></font>  <b>" + item.label + "</b>  <font color='#888888'>" + item.detail + "</font></html>");
                setFont(new Font(CURRENT_FONT, Font.PLAIN, 13));
                setBackground(isSelected ? new Color(T.ACCENT.getRed(), T.ACCENT.getGreen(), T.ACCENT.getBlue(), 60) : new Color(0,0,0,0));
                setForeground(T.TEXT);
                return this;
            }
            private String colorToHex(Color c) { return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue()); }
        }
        void showAt(JTextPane ed, Point location) { this.editor = ed; setLocation(location); if (model.getSize() > 0) { list.setSelectedIndex(0); setVisible(true); } }
        void hideCompletion() { setVisible(false); }
        void insertSelected() {
            CompletionItem sel = list.getSelectedValue();
            if (sel != null && editor != null) {
                try {
                    Document doc = editor.getDocument();
                    int caret = editor.getCaretPosition();
                    int start = caret;
                    while (start > 0 && Character.isJavaIdentifierPart(doc.getText(start - 1, 1).charAt(0))) start--;
                    doc.remove(start, caret - start);
                    doc.insertString(start, sel.snippet, null);
                    editor.setCaretPosition(start + sel.snippet.length());
                } catch (BadLocationException ex) { ex.printStackTrace(); }
            }
            hideCompletion();
        }
    }

    private void showAutocomplete(JTextPane ed) {
        if (completionPopup == null) completionPopup = new CompletionPopup(this);
        if (langBox == null) return;
        String lang = (String) langBox.getSelectedItem();
        if (lang == null) return;
        List<CompletionItem> items = COMPLETIONS.get(lang);
        if (items == null) return;
        try {
            String text = ed.getDocument().getText(0, ed.getCaretPosition());
            int start = ed.getCaretPosition();
            while (start > 0 && Character.isJavaIdentifierPart(text.charAt(start - 1))) start--;
            String prefix = text.substring(start, ed.getCaretPosition());
            DefaultListModel<CompletionItem> model = (DefaultListModel<CompletionItem>) ((JList<?>) getPrivateField(completionPopup, "list")).getModel();
            model.clear();
            for (CompletionItem it : items) if (prefix.isEmpty() || it.label.toLowerCase().startsWith(prefix.toLowerCase())) model.addElement(it);
            if (model.getSize() == 0) { completionPopup.hideCompletion(); return; }
            Point caretPos = ed.modelToView2D(ed.getCaretPosition()).getBounds().getLocation();
            SwingUtilities.convertPointToScreen(caretPos, ed);
            caretPos.y += ed.getFont().getSize() + 4;
            completionPopup.showAt(ed, caretPos);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private Object getPrivateField(Object obj, String field) {
        try { java.lang.reflect.Field f = obj.getClass().getDeclaredField(field); f.setAccessible(true); return f.get(obj); }
        catch (Exception e) { return null; }
    }

    // ══════════════════════════════════════════════════════
    // TERMINAL
    // ═══════════════════════════════════════════════════════
    private JPanel buildTermPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 0));
        p.setBackground(T.BG2);
        terminal = new JTextPane();
        terminal.setBackground(T.BG); terminal.setForeground(T.TEXT);
        terminal.setFont(new Font(CURRENT_FONT, Font.PLAIN, 13));
        terminal.setMargin(new Insets(8, 14, 8, 14));
        terminal.setEditable(false);
        termDoc = terminal.getStyledDocument();
        JScrollPane tsp = new JScrollPane(terminal);
        tsp.setBorder(null); tsp.getViewport().setBackground(T.BG);
        tsp.getVerticalScrollBar().setUI(new ThinScrollBarUI());
        p.add(tsp, BorderLayout.CENTER);
        JPanel inputRow = new JPanel(new BorderLayout(0, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(T.BG2);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(T.BORDER);
                g2.drawLine(0, 0, getWidth(), 0);
                g2.dispose();
            }
        };
        inputRow.setOpaque(false);
        inputRow.setPreferredSize(new Dimension(0, 36));
        JLabel prompt = new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setColor(T.ACCENT);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("  > ", 6, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        prompt.setFont(new Font(CURRENT_FONT, Font.BOLD, 14));
        prompt.setPreferredSize(new Dimension(32, 36));
        terminalInput = new JTextField();
        terminalInput.setBackground(T.BG2); terminalInput.setForeground(T.TEXT);
        terminalInput.setCaretColor(T.ACCENT);
        terminalInput.setFont(new Font(CURRENT_FONT, Font.PLAIN, 13));
        terminalInput.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 8));
        terminalInput.addActionListener(e -> {
            String cmd = terminalInput.getText();
            if (cmd != null && !cmd.isBlank()) { terminalHistory.add(cmd.trim()); historyIndex = terminalHistory.size(); execCmd(cmd.trim(), terminalInput); }
        });
        terminalInput.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) { if (historyIndex > 0) { historyIndex--; terminalInput.setText(terminalHistory.get(historyIndex)); } e.consume(); }
                else if (e.getKeyCode() == KeyEvent.VK_DOWN) { if (historyIndex < terminalHistory.size() - 1) { historyIndex++; terminalInput.setText(terminalHistory.get(historyIndex)); } else { historyIndex = terminalHistory.size(); terminalInput.setText(""); } e.consume(); }
                else if (e.getKeyCode() == KeyEvent.VK_TAB) { autoCompletePath(); e.consume(); }
            }
        });
        inputRow.add(prompt, BorderLayout.WEST);
        inputRow.add(terminalInput, BorderLayout.CENTER);
        p.add(inputRow, BorderLayout.SOUTH);
        return p;
    }

    private File getCurrentDir() { return projectRoot != null ? projectRoot : new File(System.getProperty("user.home")); }

    private void autoCompletePath() {
    String text = terminalInput.getText();
    if (text.isEmpty()) return;
    String[] parts = text.split("\\s+");
    if (parts.length == 0) return;
    String lastPart = parts[parts.length - 1];
    File dir = getCurrentDir();
    String prefix;
    if (lastPart.contains("/") || lastPart.contains("\\")) {
        int lastSep = Math.max(lastPart.lastIndexOf('/'), lastPart.lastIndexOf('\\'));
        String pathPart = lastPart.substring(0, lastSep + 1);
        prefix = lastPart.substring(lastSep + 1);
        File pathDir = new File(dir, pathPart);
        if (pathDir.exists() && pathDir.isDirectory()) dir = pathDir;
    } else {
        prefix = lastPart;
    }
    
    // ✅ Создаём final-переменные для лямбды
    final File searchDir = dir;
    final String searchPrefix = prefix.toLowerCase();
    
    File[] files = searchDir.listFiles((d, name) -> name.toLowerCase().startsWith(searchPrefix));
    if (files != null && files.length > 0) {
        if (files.length == 1) {
            String completion = files[0].getName();
            if (files[0].isDirectory()) completion += File.separator;
            terminalInput.setText(text.substring(0, text.length() - prefix.length()) + completion);
        } else {
            tprint("\nMatches:\n", T.TEXT3);
            for (File f : files) tprint("    " + f.getName() + (f.isDirectory() ? "/" : "") + "\n", T.TEXT2);
        }
    }
}

    private void execCmd(String cmd, JTextField input) {
        if (cmd == null || cmd.isBlank()) return;
        String c = cmd.trim();
        tprint("\n  > " + c + "\n", T.ACCENT);
        input.setText("");
        if (c.equals("cd") || c.startsWith("cd ")) { handleChangeDir(c); return; }
        if (c.equals("pwd")) { tprint("  " + getCurrentDir().getAbsolutePath() + "\n", T.TEXT); return; }
        if (c.equals("clear") || c.equals("cls")) { clearTerm(); return; }
        if (c.equals("ls") || c.equals("dir")) { handleListDir(); return; }
        new SwingWorker<Void, String>() {
            protected Void doInBackground() throws Exception {
                String[] sh = Cfg.isWin() ? new String[]{"cmd","/c",c} : new String[]{"/bin/sh","-c",c};
                ProcessBuilder pb = new ProcessBuilder(sh).redirectErrorStream(true);
                pb.directory(getCurrentDir());
                Process proc = pb.start();
                try (BufferedReader r = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
                    String line;
                    while ((line = r.readLine()) != null) publish(line + "\n");
                }
                publish("\n--- exit " + proc.waitFor() + " ---\n");
                return null;
            }
            protected void process(List<String> chunks) {
                for (String s : chunks) {
                    boolean isErr = s.toLowerCase().contains("error") || s.contains("exit 1") || s.contains("Exception");
                    tprint(s, isErr ? T.RED : T.TEXT);
                    String explanation = ErrorExplainer.explain(s);
                    if (!explanation.isEmpty()) tprint("  > " + explanation + "\n", T.YELLOW);
                }
            }
            protected void done() { SwingUtilities.invokeLater(() -> { if (terminalInput != null) terminalInput.requestFocusInWindow(); }); }
        }.execute();
    }

    private void handleChangeDir(String cmd) {
        String[] parts = cmd.split("\\s+", 2);
        if (parts.length < 2) { tprint("  " + System.getProperty("user.home") + "\n", T.TEXT); return; }
        String path = parts[1].trim();
        File newDir = path.equals("..") ? getCurrentDir().getParentFile() : path.equals("~") ? new File(System.getProperty("user.home")) : new File(getCurrentDir(), path);
        try {
            newDir = newDir.getCanonicalFile();
            if (newDir.exists() && newDir.isDirectory()) { if (projectRoot != null) { projectRoot = newDir; refreshTree(); } tprint("  Changed to: " + newDir.getAbsolutePath() + "\n", T.GREEN); }
            else tprint("  No such directory: " + path + "\n", T.RED);
        } catch (IOException e) { tprint("  " + e.getMessage() + "\n", T.RED); }
    }

    private void handleListDir() {
        File dir = getCurrentDir();
        File[] files = dir.listFiles();
        if (files == null) return;
        Arrays.sort(files, (a, b) -> (a.isDirectory() == b.isDirectory()) ? a.getName().compareToIgnoreCase(b.getName()) : (a.isDirectory() ? -1 : 1));
        for (File f : files) if (!f.isHidden()) tprint("  " + (f.isDirectory() ? "[DIR] " : "      ") + f.getName() + "\n", f.isDirectory() ? T.BLUE : T.TEXT);
    }

    // ═══════════════════════════════════════════════════════
    // SYNTAX HIGHLIGHTING
    // ═══════════════════════════════════════════════════════
    private void scheduleHl(JTextPane ed) {
        if (hlTimer != null) hlTimer.stop();
        hlTimer = new javax.swing.Timer(200, e -> applyHl(ed));
        hlTimer.setRepeats(false);
        hlTimer.start();
    }

    private void applyHl(JTextPane ed) {
        if (langBox == null) return;
        StyledDocument doc = ed.getStyledDocument();
        String fullText;
        try { fullText = doc.getText(0, doc.getLength()); } catch (BadLocationException e) { return; }
        SimpleAttributeSet base = new SimpleAttributeSet();
        StyleConstants.setForeground(base, T.TEXT);
        doc.setCharacterAttributes(0, fullText.length(), base, true);
        String lang = (String) langBox.getSelectedItem();
        if (lang == null) return;
        if (lang.equals("Python") || lang.equals("Ruby") || lang.equals("Bash")) hl(doc, fullText, "#[^\\n]*", T.C_COMMENT, 0, fullText.length());
        else if (lang.equals("HTML/CSS/JS")) hl(doc, fullText, "<!--[\\s\\S]*?-->", T.C_COMMENT, 0, fullText.length());
        else hl(doc, fullText, "//[^\\n]*", T.C_COMMENT, 0, fullText.length());
        if (lang.equals("Java") || lang.equals("C") || lang.equals("C++") || lang.equals("JavaScript") || lang.equals("Go") || lang.equals("Rust") || lang.equals("Kotlin")) hl(doc, fullText, "/\\*[\\s\\S]*?\\*/", T.C_COMMENT, 0, fullText.length());
        hl(doc, fullText, "\"[^\"\\\\]*(?:\\\\.[^\"\\\\]*)*\"|'[^'\\\\]*(?:\\\\.[^'\\\\]*)*'", T.C_STRING, 0, fullText.length());
        hl(doc, fullText, "\\b\\d+\\.?\\d*\\b", T.C_NUMBER, 0, fullText.length());
        String kw = switch (lang) {
            case "Java" -> "\\b(public|private|protected|class|interface|static|final|void|new|return|if|else|for|while|try|catch|import|package|this|super|null|true|false|int|String)\\b";
            case "Python" -> "\\b(def|class|if|elif|else|for|while|return|import|from|as|try|except|with|lambda|pass|break|continue|None|True|False|self|async|await)\\b";
            case "Kotlin" -> "\\b(fun|val|var|class|interface|object|when|if|else|for|while|return|import|package|null|true|false|this|super|is|as|in|public|private|protected|internal|open|final|abstract|data|sealed|companion)\\b";
            case "JavaScript","TypeScript" -> "\\b(const|let|var|function|class|extends|return|if|else|for|while|import|export|async|await|try|catch|new|this|null|undefined|true|false)\\b";
            case "C","C++" -> "\\b(int|long|double|float|char|void|bool|auto|const|static|return|if|else|for|while|struct|class|namespace|using|include|new|delete|nullptr|true|false)\\b";
            case "Rust" -> "\\b(fn|let|mut|pub|use|mod|struct|enum|impl|trait|for|while|loop|if|else|match|return|self|Self|true|false)\\b";
            case "Go" -> "\\b(func|var|const|type|struct|interface|package|import|if|else|for|range|return|switch|case|go|defer|nil|true|false)\\b";
            default -> null;
        };
        if (kw != null) hl(doc, fullText, kw, T.C_KEYWORD, 0, fullText.length());
        hl(doc, fullText, "\\b[a-zA-Z_][a-zA-Z0-9_]*(?=\\s*\\()", T.C_FUNCTION, 0, fullText.length());
        hl(doc, fullText, "\\b[A-Z][a-zA-Z0-9]*\\b", T.C_CLASS, 0, fullText.length());
    }

    private void hl(StyledDocument doc, String text, String regex, Color c, int rs, int re) {
        SimpleAttributeSet a = new SimpleAttributeSet();
        StyleConstants.setForeground(a, c);
        try {
            Matcher m = cached(regex).matcher(text);
            while (m.find()) {
                int s = m.start(), e = m.end();
                if (e <= rs) continue;
                if (s >= re) break;
                doc.setCharacterAttributes(Math.max(s, rs), Math.min(e, re) - Math.max(s, rs), a, false);
            }
        } catch (Exception ignored) {}
    }

    class LineGutter extends JPanel implements DocumentListener, CaretListener {
        private final JTextComponent ed;
        LineGutter(JTextComponent ed) {
            this.ed = ed;
            setPreferredSize(new Dimension(56, 0));
            setBackground(T.BG);
            setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, T.BORDER));
            ed.getDocument().addDocumentListener(this);
            ed.addCaretListener(this);
        }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setFont(new Font(CURRENT_FONT, Font.PLAIN, 13));
            FontMetrics fm = g2.getFontMetrics();
            Rectangle clip = g.getClipBounds();
            Element root = ed.getDocument().getDefaultRootElement();
            int s = root.getElementIndex(ed.viewToModel2D(new Point(0, clip.y)));
            int e = root.getElementIndex(ed.viewToModel2D(new Point(0, clip.y + clip.height)));
            int caret = root.getElementIndex(ed.getCaretPosition());
            for (int i = s; i <= e; i++) {
                try {
                    Rectangle r = ed.modelToView2D(root.getElement(i).getStartOffset()).getBounds();
                    if (i == caret) { g2.setColor(T.ACCENT); g2.fillRect(0, r.y, getWidth(), r.height); g2.setColor(T.BG); }
                    else g2.setColor(T.TEXT3);
                    String ln = String.valueOf(i + 1);
                    g2.drawString(ln, getWidth() - fm.stringWidth(ln) - 10, r.y + r.height - fm.getDescent());
                } catch (BadLocationException ignored) {}
            }
            g2.dispose();
        }
        void upd() { SwingUtilities.invokeLater(this::repaint); }
        public void insertUpdate(DocumentEvent e) { upd(); }
        public void removeUpdate(DocumentEvent e) { upd(); }
        public void changedUpdate(DocumentEvent e) {}
        public void caretUpdate(CaretEvent e) { upd(); }
    }

    // ═══════════════════════════════════════════════════════
    // STATUS BAR
    // ═══════════════════════════════════════════════════════
    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(0, 0, T.GRAD1, getWidth(), 0, T.GRAD2);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        bar.setOpaque(false);
        bar.setPreferredSize(new Dimension(0, 26));
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setOpaque(false);
        left.add(statusItem("main", true));
        bar.add(left, BorderLayout.WEST);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        cursorLbl = new JLabel("  Ln 1, Col 1  ");
        cursorLbl.setFont(new Font("Dialog", Font.PLAIN, 11));
        cursorLbl.setForeground(Color.WHITE);
        right.add(cursorLbl);
        bar.add(right, BorderLayout.EAST);
        statusLbl = new JLabel("  LumixCode - Ready");
        statusLbl.setFont(new Font("Dialog", Font.BOLD, 11));
        statusLbl.setForeground(Color.WHITE);
        bar.add(statusLbl, BorderLayout.CENTER);
        return bar;
    }

    private JLabel statusItem(String t, boolean bold) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Dialog", bold ? Font.BOLD : Font.PLAIN, 11));
        l.setForeground(new Color(255,255,255,230));
        l.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        return l;
    }

    private void setStatus(String t) { SwingUtilities.invokeLater(() -> statusLbl.setText("  " + t)); }

    private void updateCursor(JTextPane ed) {
        if (cursorLbl == null) return;
        int pos = ed.getCaretPosition();
        try {
            StyledDocument doc = ed.getStyledDocument();
            int line = doc.getDefaultRootElement().getElementIndex(pos) + 1;
            int lineStart = doc.getDefaultRootElement().getElement(line-1).getStartOffset();
            cursorLbl.setText("  Ln " + line + ", Col " + (pos - lineStart + 1) + "  ");
        } catch (Exception ignored) {}
    }

    private void applyNewTheme(String name) {
        T = THEMES.getOrDefault(name, THEMES.get("Lumix Ember"));
        Cfg.setTheme(name);
        SwingUtilities.updateComponentTreeUI(this);
        getContentPane().setBackground(T.BG);
        repaint();
        tprint(" Theme -> " + name + "\n", T.ACCENT);
    }

    private void cycleTheme() {
        List<String> keys = new ArrayList<>(THEMES.keySet());
        int idx = keys.indexOf(T.name);
        String next = keys.get((idx + 1) % keys.size());
        applyNewTheme(next);
    }

    // ═══════════════════════════════════════════════════════
    // FILE OPERATIONS
    // ═══════════════════════════════════════════════════════
    private boolean isSafeName(String name) {
        if (name == null || name.isBlank()) return false;
        name = name.trim();
        if (name.equals(".") || name.equals("..")) return false;
        return !name.matches(".*[/\\\\:*?\"<>|].*");
    }

    private boolean isPathSafe(File base, File target) {
        try { return target.getCanonicalPath().startsWith(base.getCanonicalPath()); }
        catch (IOException e) { return false; }
    }

    private void openProject() {
        JFileChooser fc = new JFileChooser(Cfg.getLastDir());
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            projectRoot = fc.getSelectedFile();
            Cfg.setLastDir(projectRoot.getAbsolutePath());
            setTitle("LumixCode - " + projectRoot.getName());
            refreshTree();
            tprint(" Project opened: " + projectRoot.getAbsolutePath() + "\n", T.GREEN);
        }
    }

    private void openFile() {
        JFileChooser fc = new JFileChooser(Cfg.getLastDir());
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selected = fc.getSelectedFile();
            if (projectRoot != null && !isPathSafe(projectRoot, selected)) { tprint(" Security: outside project\n", T.RED); return; }
            openFileInEditor(selected);
        }
    }

    private void saveFile() {
        if (activeEd == null) return;
        File known = editorFiles.get(activeEd);
        if (known != null) {
            executorService.submit(() -> {
                try {
                    Path tempPath = known.toPath().resolveSibling(known.getName() + ".tmp");
                    Files.writeString(tempPath, activeEd.getText());
                    Files.move(tempPath, known.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
                    SwingUtilities.invokeLater(() -> { editorModified.put(activeEd, false); updateTabAfterSave(activeEd); tprint(" Saved -> " + known.getName() + "\n", T.GREEN); });
                } catch (IOException e) { SwingUtilities.invokeLater(() -> tprint(" " + e.getMessage() + "\n", T.RED)); }
            });
            return;
        }
        saveFileAs();
    }

    private void saveFileAs() {
        if (activeEd == null) return;
        int idx = tabs.getSelectedIndex();
        if (idx < 0) return;
        JFileChooser fc = new JFileChooser(Cfg.getLastDir());
        fc.setSelectedFile(new File(tabs.getTitleAt(idx).replace("* ", "")));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            executorService.submit(() -> {
                try {
                    Files.writeString(f.toPath(), activeEd.getText());
                    SwingUtilities.invokeLater(() -> { editorFiles.put(activeEd, f); editorModified.put(activeEd, false); updateTabAfterSave(activeEd); tprint(" Saved -> " + f.getName() + "\n", T.GREEN); });
                } catch (IOException e) { SwingUtilities.invokeLater(() -> tprint(" " + e.getMessage() + "\n", T.RED)); }
            });
        }
    }

    private void updateTabAfterSave(JTextPane ed) {
        for (int i = 0; i < tabs.getTabCount(); i++) {
            Component comp = tabs.getComponentAt(i);
            if (comp instanceof JScrollPane sp && sp.getViewport().getView() == ed) {
                String title = tabs.getTitleAt(i);
                if (title.startsWith("* ")) tabs.setTitleAt(i, title.substring(2));
                comp.repaint();
                break;
            }
        }
    }

    private void openFileInEditor(File f) {
        for (int i = 0; i < tabs.getTabCount(); i++) {
            Component comp = tabs.getComponentAt(i);
            if (comp instanceof JScrollPane sp && sp.getViewport().getView() instanceof JTextPane tp) {
                File existing = editorFiles.get(tp);
                if (existing != null && existing.getAbsolutePath().equals(f.getAbsolutePath())) { tabs.setSelectedIndex(i); return; }
            }
        }
        executorService.submit(() -> {
            try {
                String content = Files.readString(f.toPath());
                SwingUtilities.invokeLater(() -> { addNewTab(f.getName(), content, f); });
            } catch (IOException e) { SwingUtilities.invokeLater(() -> tprint(" " + e.getMessage() + "\n", T.RED)); }
        });
    }

    private void createNewFile(File parent) {
        String name = JOptionPane.showInputDialog(this, "File name:");
        if (!isSafeName(name)) return;
        try { File f = new File(parent, name.trim()); if (f.createNewFile()) { refreshTree(); openFileInEditor(f); } } catch (IOException e) { tprint(" " + e.getMessage() + "\n", T.RED); }
    }

    private void createNewFolder(File parent) {
        String name = JOptionPane.showInputDialog(this, "Folder name:");
        if (!isSafeName(name)) return;
        new File(parent, name.trim()).mkdir();
        refreshTree();
    }

    private void renameFile(File f) {
        String n = JOptionPane.showInputDialog(this, "New name:", f.getName());
        if (!isSafeName(n)) return;
        File newFile = new File(f.getParentFile(), n.trim());
        if (f.renameTo(newFile)) refreshTree();
    }

    private void deleteFile(File f) {
        int ok = JOptionPane.showConfirmDialog(this, "Delete " + f.getName() + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) { delRec(f); refreshTree(); }
    }

    private void delRec(File f) { if (f.isDirectory()) { File[] ch = f.listFiles(); if (ch != null) for (File c : ch) delRec(c); } f.delete(); }

    private void refreshTree() {
        if (projectRoot == null || !projectRoot.exists()) return;
        treeModel = new DefaultTreeModel(buildNode(projectRoot));
        fileTree.setModel(treeModel);
        expandAll(fileTree, new TreePath(treeModel.getRoot()), true);
    }

    private DefaultMutableTreeNode buildNode(File dir) {
        DefaultMutableTreeNode n = new DefaultMutableTreeNode(dir);
        File[] files = dir.listFiles();
        if (files != null) {
            Arrays.sort(files, (a, b) -> (a.isDirectory() == b.isDirectory()) ? a.getName().compareToIgnoreCase(b.getName()) : (a.isDirectory() ? -1 : 1));
            for (File f : files) if (!f.getName().startsWith(".")) n.add(f.isDirectory() ? buildNode(f) : new DefaultMutableTreeNode(f));
        }
        return n;
    }

    private void expandAll(JTree tree, TreePath parent, boolean expand) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) parent.getLastPathComponent();
        for (Enumeration<?> e = node.children(); e.hasMoreElements(); ) expandAll(tree, parent.pathByAddingChild(e.nextElement()), expand);
        if (expand) tree.expandPath(parent);
    }

    private void showQuickOpen() {
        if (projectRoot == null) { tprint(" Open a folder first\n", T.WARNING); return; }
        String query = JOptionPane.showInputDialog(this, "Quick Open:");
        if (query == null || query.isBlank()) return;
        final String q = query.toLowerCase();
        List<File> matches = new ArrayList<>();
        try { Files.walk(projectRoot.toPath()).map(Path::toFile).filter(f -> f.isFile() && !f.isHidden() && f.getName().toLowerCase().contains(q)).limit(50).forEach(matches::add); } catch (IOException e) {}
        if (matches.isEmpty()) { tprint(" No matches\n", T.WARNING); return; }
        Object[] opts = matches.stream().map(f -> projectRoot.toPath().relativize(f.toPath()).toString()).toArray();
        int r = JOptionPane.showOptionDialog(this, "Select:", "Quick Open", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opts, opts[0]);
        if (r >= 0) openFileInEditor(matches.get(r));
    }

    private void showCommandPalette() {
        String[] cmds = {"New File", "Open File", "Open Folder", "Save", "Quick Open", "Find", "Replace", "Go to Line", "Run Code", "Change Theme", "Change Font", "Live Server", "Clear Terminal"};
        int r = JOptionPane.showOptionDialog(this, "Command Palette:", "Commands", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, cmds, cmds[0]);
        if (r < 0) return;
        switch (r) {
            case 0 -> newTab(); case 1 -> openFile(); case 2 -> openProject(); case 3 -> saveFile();
            case 4 -> showQuickOpen(); case 5 -> showFind(); case 6 -> showReplace(); case 7 -> showGotoLine();
            case 8 -> runCode(); case 9 -> cycleTheme(); case 10 -> showFontDialog(); case 11 -> toggleLive(); case 12 -> clearTerm();
        }
    }

    // ═══════════════════════════════════════════════════════
    // RUN CODE (Java, C++, Python, JS из коробки)
    // ═══════════════════════════════════════════════════════
    private void runCode() {
        if (activeEd == null) { tprint(" No active editor\n", T.RED); return; }
        String lang = (String) langBox.getSelectedItem();
        String code = activeEd.getText();
        if (code == null || code.isBlank()) { tprint(" Empty file\n", T.RED); return; }
        if ("HTML/CSS/JS".equals(lang)) { toggleLive(); return; }
        tprint("\n-- Running " + lang + " [" + runConfig + "] --\n", T.TEXT3);
        setStatus("Running " + lang + "...");
        new SwingWorker<Void, String>() {
            protected Void doInBackground() throws Exception {
                File tmp = Files.createTempDirectory("lumix_").toFile(); tmp.deleteOnExit();
                String[] cmd = buildCmd(lang, code, tmp);
                if (cmd == null) { publish(" " + lang + " runner not found\n"); return null; }
                ProcessBuilder pb = new ProcessBuilder(cmd).redirectErrorStream(true);
                pb.directory(tmp);
                runProcess = pb.start();
                try (BufferedReader r = new BufferedReader(new InputStreamReader(runProcess.getInputStream()))) {
                    String line;
                    while ((line = r.readLine()) != null) publish(line + "\n");
                }
                publish("\n-- exit " + runProcess.waitFor() + " --\n");
                return null;
            }
            protected void process(List<String> chunks) {
                for (String s : chunks) {
                    boolean isErr = s.toLowerCase().contains("error") || s.contains("exit 1") || s.contains("Exception");
                    tprint(s, isErr ? T.RED : T.TEXT);
                    String explanation = ErrorExplainer.explain(s);
                    if (!explanation.isEmpty()) tprint("  > " + explanation + "\n", T.YELLOW);
                }
            }
            protected void done() { try { get(); setStatus(" Done"); } catch (Exception e) { setStatus(" Error"); tprint(" " + e.getMessage() + "\n", T.RED); } runProcess = null; }
        }.execute();
    }

    private String[] buildCmd(String lang, String code, File dir) throws IOException {
        return switch (lang) {
            case "Java" -> {
                Matcher m = cached("(?:public\\s+)?class\\s+(\\w+)").matcher(code);
                String cls = (m.find() && m.group(1).matches("[a-zA-Z_][a-zA-Z0-9_]*")) ? m.group(1) : "Main";
                File src = new File(dir, cls + ".java");
                Files.writeString(src.toPath(), code);
                String jc = "javac -encoding UTF-8 \"" + src.getAbsolutePath() + "\" -d \"" + dir.getAbsolutePath() + "\" && java -cp \"" + dir.getAbsolutePath() + "\" " + cls;
                yield Cfg.isWin() ? new String[]{"cmd","/c", jc} : new String[]{"/bin/sh","-c", jc.replace("\"","'")};
            }
            case "Python" -> {
                File f = new File(dir,"main.py"); Files.writeString(f.toPath(), code);
                String py = venvPath != null ? (Cfg.isWin() ? venvPath + "\\Scripts\\python.exe" : venvPath + "/bin/python") : Cfg.findCmd("python3","python","py");
                yield new String[]{py, f.getAbsolutePath()};
            }
            case "Kotlin" -> {
                File f = new File(dir,"Main.kt"); Files.writeString(f.toPath(), code);
                String kc = Cfg.findCmd("kotlinc") + " \"" + f.getAbsolutePath() + "\" -include-runtime -d \"" + dir.getAbsolutePath() + "/Main.jar\" && java -jar \"" + dir.getAbsolutePath() + "/Main.jar\"";
                yield Cfg.isWin() ? new String[]{"cmd","/c",kc} : new String[]{"/bin/sh","-c",kc.replace("\"","'")};
            }
            case "JavaScript","TypeScript" -> { File f = new File(dir,"main.js"); Files.writeString(f.toPath(), code); yield new String[]{Cfg.findCmd("node"), f.getAbsolutePath()}; }
            case "C++" -> {
                File f = new File(dir,"main.cpp"); Files.writeString(f.toPath(), code);
                String out = new File(dir, Cfg.isWin()?"main.exe":"main.out").getAbsolutePath();
                String c = Cfg.findCmd("g++","clang++") + " -std=c++20 \"" + f.getAbsolutePath() + "\" -o \"" + out + "\" && \"" + out + "\"";
                yield Cfg.isWin() ? new String[]{"cmd","/c",c} : new String[]{"/bin/sh","-c",c.replace("\"","'")};
            }
            case "C" -> {
                File f = new File(dir,"main.c"); Files.writeString(f.toPath(), code);
                String out = new File(dir, Cfg.isWin()?"main.exe":"main.out").getAbsolutePath();
                String c = Cfg.findCmd("gcc","clang") + " \"" + f.getAbsolutePath() + "\" -o \"" + out + "\" && \"" + out + "\"";
                yield Cfg.isWin() ? new String[]{"cmd","/c",c} : new String[]{"/bin/sh","-c",c.replace("\"","'")};
            }
            case "Rust" -> {
                File f = new File(dir,"main.rs"); Files.writeString(f.toPath(), code);
                String out = new File(dir,"main").getAbsolutePath();
                String c = "rustc \"" + f.getAbsolutePath() + "\" -o \"" + out + (Cfg.isWin()?".exe":"") + "\" && \"" + out + (Cfg.isWin()?".exe":"") + "\"";
                yield Cfg.isWin() ? new String[]{"cmd","/c",c} : new String[]{"/bin/sh","-c",c.replace("\"","'")};
            }
            case "Go" -> { File f = new File(dir,"main.go"); Files.writeString(f.toPath(), code); yield new String[]{"go","run", f.getAbsolutePath()}; }
            default -> null;
        };
    }

    // ═══════════════════════════════════════════════════════
    // LIVE SERVER (для HTML/CSS/JS)
    // ═══════════════════════════════════════════════════════
    private void toggleLive() {
        if (liveOn) { if (liveServer != null) liveServer.stop(0); liveOn = false; tprint(" Live Server stopped\n", T.YELLOW); return; }
        try {
            int port = Cfg.getLivePort();
            Path root = Files.createTempDirectory("lumix_live_");
            Files.writeString(root.resolve("index.html"), injectReload(activeEd.getText()));
            liveServer = HttpServer.create(new InetSocketAddress("127.0.0.1", port), 0);
            liveServer.createContext("/", ex -> {
                Path requested = root.resolve("index.html");
                byte[] body = Files.readAllBytes(requested);
                ex.getResponseHeaders().add("Content-Type", "text/html;charset=utf-8");
                ex.getResponseHeaders().add("Cache-Control", "no-cache");
                ex.sendResponseHeaders(200, body.length);
                ex.getResponseBody().write(body);
                ex.getResponseBody().close();
            });
            liveServer.setExecutor(Executors.newCachedThreadPool());
            liveServer.start();
            liveOn = true;
            String url = "http://127.0.0.1:" + port;
            tprint(" Live Server -> " + url + "\n", T.CYAN);
            if (Desktop.isDesktopSupported()) Desktop.getDesktop().browse(new URI(url));
            Thread watcher = new Thread(() -> {
                long last = 0;
                while (liveOn) {
                    try {
                        Thread.sleep(400);
                        if (activeEd != null) {
                            long cur = activeEd.getText().hashCode();
                            if (cur != last) { Files.writeString(root.resolve("index.html"), injectReload(activeEd.getText())); last = cur; }
                        }
                    } catch (Exception ignored) {}
                }
            }, "LiveWatcher");
            watcher.setDaemon(true);
            watcher.start();
        } catch (Exception e) { tprint(" " + e.getMessage() + "\n", T.RED); }
    }

    private String injectReload(String html) {
        String s = "<script>setInterval(async()=>{try{const r=await fetch(location.href,{cache:'no-store'});const t=await r.text();if(window._t&&window._t!==t)location.reload();window._t=t;}catch(e){}},800);</script>";
        return html.contains("</body>") ? html.replace("</body>", s + "</body>") : html + s;
    }

    // ══════════════════════════════════════════════════════
    // UTILS
    // ═══════════════════════════════════════════════════════
    private void tprint(String text, Color c) {
        SwingUtilities.invokeLater(() -> {
            SimpleAttributeSet a = new SimpleAttributeSet();
            StyleConstants.setForeground(a, c);
            StyleConstants.setFontFamily(a, CURRENT_FONT);
            StyleConstants.setFontSize(a, 13);
            try { termDoc.insertString(termDoc.getLength(), text, a); terminal.setCaretPosition(termDoc.getLength()); }
            catch (BadLocationException ignored) {}
        });
    }

    private void clearTerm() { SwingUtilities.invokeLater(() -> { try { termDoc.remove(0, termDoc.getLength()); } catch (BadLocationException ignored) {} }); }

    private void printWelcome() {
        tprint("  LumixCode - Ultimate Edition\n", T.ACCENT);
        tprint("  ------------------------------------------\n", T.TEXT3);
        tprint("  Ctrl+Enter   Run code\n", T.TEXT2);
        tprint("  Ctrl+S       Save file\n", T.TEXT2);
        tprint("  Ctrl+P       Quick Open\n", T.TEXT2);
        tprint("  Ctrl+Space   Autocomplete\n", T.TEXT2);
        tprint("  Ctrl+F       Find in Code\n", T.TEXT2);
        tprint("  Ctrl+G       Go to Line\n", T.TEXT2);
        tprint("  Ctrl+Z       Zen Mode\n", T.TEXT2);
        tprint("  ------------------------------------------\n", T.TEXT3);
        tprint("  Languages: Java, Python, Kotlin, JS/TS, C/C++, Rust, Go, HTML\n", T.TEXT2);
        tprint("  Live Server for HTML/CSS/JS on port 5500\n", T.TEXT2);
        tprint("  Working directory: " + getCurrentDir().getAbsolutePath() + "\n", T.TEXT2);
    }

    private void checkTools() {
        for (String t : new String[]{"javac","python","python3","py","node","g++","rustc","go","kotlinc"})
            if (!Cfg.cmdExists(t)) SwingUtilities.invokeLater(() -> tprint("  " + t + " not found\n", T.YELLOW));
    }

    private void setupShortcuts() {
        JRootPane r = getRootPane();
        bind(r, KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK, "run", e -> runCode());
        bind(r, KeyEvent.VK_S,     InputEvent.CTRL_DOWN_MASK, "save", e -> saveFile());
        bind(r, KeyEvent.VK_F,     InputEvent.CTRL_DOWN_MASK, "find", e -> showFind());
        bind(r, KeyEvent.VK_N,     InputEvent.CTRL_DOWN_MASK, "newtab", e -> newTab());
        bind(r, KeyEvent.VK_P,     InputEvent.CTRL_DOWN_MASK, "quickopen", e -> showQuickOpen());
        bind(r, KeyEvent.VK_P,     InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK, "cmdpalette", e -> showCommandPalette());
        bind(r, KeyEvent.VK_G,     InputEvent.CTRL_DOWN_MASK, "gotoline", e -> showGotoLine());
        bind(r, KeyEvent.VK_W,     InputEvent.CTRL_DOWN_MASK, "closetab", e -> closeCurrentTab());
        bind(r, KeyEvent.VK_SPACE, InputEvent.CTRL_DOWN_MASK, "autocomplete", e -> { if (activeEd != null) showAutocomplete(activeEd); });
        bind(r, KeyEvent.VK_T,     InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK, "focusTerm", e -> { if (terminalInput != null) terminalInput.requestFocusInWindow(); });
        bind(r, KeyEvent.VK_Z,     InputEvent.CTRL_DOWN_MASK, "zenmode", e -> toggleZenMode());
        bind(r, KeyEvent.VK_ESCAPE,0, "hidesearch", e -> { if (completionPopup != null && completionPopup.isVisible()) completionPopup.hideCompletion(); });
    }

    private void bind(JRootPane r, int key, int mod, String name, ActionListener a) {
        r.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key, mod), name);
        r.getActionMap().put(name, new AbstractAction() { public void actionPerformed(ActionEvent e) { a.actionPerformed(e); } });
    }

    private void showFind() {
        if (activeEd == null) return;
        String q = JOptionPane.showInputDialog(this, "Find:");
        if (q == null || q.isEmpty()) return;
        String t = activeEd.getText();
        int i = t.indexOf(q, activeEd.getCaretPosition());
        if (i < 0) i = t.indexOf(q);
        if (i >= 0) { activeEd.setCaretPosition(i); activeEd.moveCaretPosition(i + q.length()); }
        else tprint("  Not found: " + q + "\n", T.YELLOW);
    }

    private void showReplace() {
        if (activeEd == null) return;
        String find = JOptionPane.showInputDialog(this, "Find:");
        if (find == null || find.isEmpty()) return;
        String replace = JOptionPane.showInputDialog(this, "Replace with:");
        if (replace == null) return;
        activeEd.setText(activeEd.getText().replace(find, replace));
        tprint(" Replaced\n", T.GREEN);
    }

    private void showGotoLine() {
        if (activeEd == null) return;
        String ln = JOptionPane.showInputDialog(this, "Go to line:");
        if (ln == null || ln.isBlank()) return;
        try {
            int line = Integer.parseInt(ln.trim()) - 1;
            StyledDocument doc = activeEd.getStyledDocument();
            Element root = doc.getDefaultRootElement();
            if (line >= 0 && line < root.getElementCount()) activeEd.setCaretPosition(root.getElement(line).getStartOffset());
        } catch (Exception e) { tprint(" Invalid line\n", T.RED); }
    }

    private void showFontDialog() {
        String[] fontOpts = new String[FONTS.length];
        for (int i = 0; i < FONTS.length; i++) fontOpts[i] = FONTS[i] + (FONTS[i].equals(CURRENT_FONT) ? " *" : "");
        int r = JOptionPane.showOptionDialog(this, "Select font:", "Font", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, fontOpts, fontOpts[0]);
        if (r >= 0) { CURRENT_FONT = FONTS[r]; Cfg.setFont(CURRENT_FONT); updateAllFonts(); tprint(" Font -> " + CURRENT_FONT + "\n", T.ACCENT); }
    }

    private void updateAllFonts() {
        Font newFont = new Font(CURRENT_FONT, Font.PLAIN, FONT_SIZE);
        for (int i = 0; i < tabs.getTabCount(); i++) {
            Component comp = tabs.getComponentAt(i);
            if (comp instanceof JScrollPane sp && sp.getViewport().getView() instanceof JTextPane tp) tp.setFont(newFont);
        }
        if (terminal != null) terminal.setFont(newFont);
    }

    private void toggleZenMode() {
        zenMode = !zenMode;
        if (zenMode) {
            tprint(" Zen Mode ON - Focus on code\n", T.GREEN);
            setStatus(" Zen Mode");
        } else {
            tprint(" Zen Mode OFF\n", T.YELLOW);
            setStatus(" LumixCode - Ready");
        }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(LumixCode::new);
    }
}