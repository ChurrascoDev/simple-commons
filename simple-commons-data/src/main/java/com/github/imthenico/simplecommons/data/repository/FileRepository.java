package com.github.imthenico.simplecommons.data.repository;

import com.github.imthenico.simplecommons.data.repository.exception.ReadException;
import com.github.imthenico.simplecommons.data.repository.exception.SerializationException;
import com.github.imthenico.simplecommons.data.repository.exception.UnknownTargetException;
import com.github.imthenico.simplecommons.data.repository.exception.WriteException;
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
            File folder,
            String fileExtension,
            GenericMapper<String> mapper
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
    public void save(T obj, String key) {
        File found = findFile(key, true);

        try {
            String content = mapper.serialize(obj);

            try {
                write(found, content);
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String id) throws UnknownTargetException {
        File found = findFile(id, false);

        if (!found.exists())
            throw new UnknownTargetException(String.format("unknown file (%s)", id));

        if (!found.delete())
            throw new UnsupportedOperationException(String.format("unable to delete file %s", id));
    }

    @Override
    public T usingId(String key) {
        File found = findFile(key, false);

        if (!found.exists())
            return null;

        return map(found);
    }

    @Override
    public Set<T> all() {
        Set<T> all = new HashSet<>();

        forEachFile(folder, file -> {
            if (file.isFile()) {
                all.add(map(file));
            }
        });

        return all;
    }

    @Override
    public Set<String> keys() {
        Set<String> all = new HashSet<>();

        forEachFile(folder, (file) -> all.add(file.getName()));

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

    private File findFile(String id, boolean create) {
        String name = id.endsWith(fileExtension) ? id : id + fileExtension;

        File found = new File(folder, name);

        try {
            if (found.exists()) {
                if (!found.isFile())
                    throw new IllegalArgumentException("invalid document (is a directory)");
            } else if (create) {
                Validate.isTrue(found.createNewFile(), String.format("unable to create file '%s'", name));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return found;
    }

    public String read(File toRead) throws ReadException {
        try {
            return FileUtils.readTextFile(toRead);
        } catch (IOException e) {
            throw new ReadException(e);
        }
    }

    public void write(File source, String data) throws WriteException {
        try {
            FileUtils.setTextContent(source, data);
        } catch (IOException e) {
            throw new WriteException(e);
        }
    }

    private String raw(File file) {
        try {
            return read(file);
        } catch (ReadException e) {
            e.printStackTrace();
        }

        return null;
    }

    private T map(File file) {
        return mapper.map(raw(file), modelClass);
    }

    private boolean isApt(File file) {
        return file.getName().endsWith(fileExtension);
    }
}