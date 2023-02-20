
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Tree {
    private final TreeFolder folder;
    private static final String DEFAULT_PATH = "/home/shlomo/git/fs";
    private Factory<Boolean, TreePrintable, PearWapper<String, Integer>> factory = new Factory<>();{
        factory.add(true, (wrap) -> new TreeFolder(wrap.data1,wrap.data2));
        factory.add(false, (wrap) -> new TreeFile(wrap.data1,wrap.data2));
    }
    public Tree() {
        this(DEFAULT_PATH);
    }
    public Tree(String pathName) {
        folder = new TreeFolder(pathName, 0);
    }

    public void printTree() {
        folder.print();
    }

    public class TreeFolder implements TreePrintable {
        private List<TreePrintable> components;
        private File file;
        private int level;

        public TreeFolder(String pathName, int level) {
            this.level = level;
            file = new File(pathName);
            components = new ArrayList<>();
            for (File runner : Objects.requireNonNull(file.listFiles())) {
                components.add(factory.create(runner.isDirectory(),new PearWapper<String, Integer>(runner.getPath(), level +1)));
            };
            }

        @Override
        public void print() {
            for (int i = 0; i < level; i++)
                System.out.print("\t");
            System.out.println(file.getName());
            for (TreePrintable treeFolder : components) {
                treeFolder.print();
            };
        }
    }

    public class TreeFile implements TreePrintable {
        private File file;
        private int level;


        public TreeFile(String filePath, int level) {
            this.file = file = new File(filePath);
            this.level = level;
        }

        @Override
        public void print() {
            for (int i = 0; i < level; i++)
                System.out.print("\t");
            System.out.println("\t" + file.getName());
        }
    }
}