package nl.danman.file_encryptor.service;

import edu.umd.cs.findbugs.annotations.NonNull;

public interface ServiceFactory {

    @NonNull
    FileServiceImpl getFileService();

    @NonNull
    AESEncryptionService getAesEncryptionService();
}
