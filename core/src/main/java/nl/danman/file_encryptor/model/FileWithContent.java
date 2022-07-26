package nl.danman.file_encryptor.model;

import java.io.File;
import java.util.Objects;

public class FileWithContent {

    public final File file;

    public final String content;

    public FileWithContent(final String fileUri, final String content) {
        this.file = new File(Objects.requireNonNull(fileUri, "File URI cannot be null"));
        this.content = Objects.requireNonNull(content, "Content cannot be null");
    }

    private FileWithContent(final File file, final String content) {
        this.file = file;
        this.content = content;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final FileWithContent file1 = (FileWithContent) o;
        return file.equals(file1.file) && content.equals(file1.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, content);
    }

    public String getUri() {
        return file.getAbsolutePath();
    }

    public FileWithContent withNewContent(final String content) {
        return new FileWithContent(file, content);
    }
}
