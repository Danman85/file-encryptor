module nl.danman.file_encryptor.service {

    requires org.apache.logging.log4j;
    requires com.github.spotbugs.annotations;

    requires org.apache.commons.io;

    exports nl.danman.file_encryptor.service;
    exports nl.danman.file_encryptor.service.impl;
    exports nl.danman.file_encryptor.repository;
    exports nl.danman.file_encryptor.repository.impl;
    exports nl.danman.file_encryptor.model;
}