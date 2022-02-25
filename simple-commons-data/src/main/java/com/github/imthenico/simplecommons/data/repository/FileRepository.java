package com.github.imthenico.simplecommons.data.repository;

import com.github.imthenico.simplecommons.data.key.SimpleSourceKey;
import com.github.imthenico.simplecommons.data.key.SourceKey;
import com.github.imthenico.simplecommons.data.mapper.GenericMapper;
import com.github.imthenico.simplecommons.data.util.FileUtils;
import com.github.imthenico.simplecommons.util.Validate;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class FileRepository<T> extends AbstractRepository<T> {

    private final File folder;
    private final String fileExtension;
    private final GenericMapper<String> mapper;

    public FileRepository(
            Executor taskProcessor,
            Class<T> modelClass,
            GenericMapper<String> mapper,
            File folder,
            String fileExtension
    ) {
        super(taskProcessor, modelClass);

        if (folder.exists()) {
            if (!folder.isDirectory())
                throw new IllegalArgumentException("the provided file is not a directory");
        } else {
            Validate.isTrue(folder.mkdirs(), "unable to create folder");
        }

        this.folder = Validate.notNull(folder);
        this.fileExtension = Validate.notNull(fileExtension);
        this.mapper = Validate.notNull(mapper);
    }

    @Override
    public void save(T obj, SourceKey key) {
        File found = findFile(key, true);

        String content = mapper.serialize(obj);

        try {
            write(found, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int delete(SourceKey key) {
        File found = findFile(key, false);

        if (!found.exists())
            return 0;

        if (!found.delete())
            throw new UnsupportedOperationException(String.format("unable to delete file %s", found.getPath()));

        return 1;
    }

    @Override
    public T usingId(SourceKey key) {
        File found = findFile(key, false);

        if (!found.exists())
            return null;

        try {
            return map(found);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Set<T> all() {
        Set<T> all = new HashSet<>();

        forEachFile(folder, file -> {
            if (!file.isFile()) {
                return;
            }

            try {
                all.add(map(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return all;
    }

    @Override
    public Set<SourceKey> keys() {
        Set<SourceKey> all = new HashSet<>();

        forEachFile(folder, (file) -> all.add(new SimpleSourceKey(file.getName(), "parentPath", file.getParent())));

        return all;
    }

    public File getFolder() {
        return folder;
    }

    private void forEachFile(File folder, Consumer<File> fileConsumer) {
        File[] files = folder.listFiles();

        if (files == null)
            return;

        for (File file : files) {
            if (!isApt(file))
                continue;

            fileConsumer.accept(file);
        }
    }

    private File findFile(SourceKey key, boolean create) {
        String fileName = key.getKey();

        if (!fileName.endsWith(fileExtension)) {
            fileName += fileExtension;
        }

        File found = new File(new File(folder, key.getExtraData("parentPath", "")), fileName);

        try {
            if (found.exists()) {
                if (!found.isFile())
                    throw new IllegalArgumentException("invalid document (is a directory)");
            } else if (create) {
                Validate.isTrue(found.createNewFile(), String.format("unable to create file '%s'", found.getAbsolutePath()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return found;
    }

    public String read(File toRead) throws IOException {
        return FileUtils.readTextFile(toRead);
    }

    public void write(File source, String data) throws IOException {
        FileUtils.setTextContent(source, data);
    }

    private T map(File file) throws IOException {
        return mapper.map(read(file), modelClass);
    }

    private boolean isApt(File file) {
        return file.getName().endsWith(fileExtension);
    }
}