package nl.danman.file_encryptor.service;

import edu.umd.cs.findbugs.annotations.NonNull;
import nl.danman.file_encryptor.service.impl.AESEncryptionService;
import nl.danman.file_encryptor.service.impl.FileWithContentService;

public interface ServiceFactory {

    @NonNull
    AESEncryptionService getAesEncryptionService();

    @NonNull
    FileWithContentService getFileWithContentService();
}
