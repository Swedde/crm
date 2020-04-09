package crm.utility;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class VFS {

    private static class FileIterator implements Iterator<File> {
        private Queue<File> files = new LinkedList<>();

        public FileIterator(String path) {
            files.add(new File(path));
        }

        public boolean hasNext() {
            return !files.isEmpty();
        }

        public File next() {
            File file = files.peek();
            if (file.isDirectory()) {
                for (File subFile : file.listFiles()) {
                    files.add(subFile);
                }
            }
            return files.poll();
        }

        public void remove() {
        }
    }

    public static Iterator<File> getIterator(String startDir) {
        return new FileIterator(startDir);
    }
}
