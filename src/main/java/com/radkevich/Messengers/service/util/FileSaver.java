package com.radkevich.Messengers.service.util;

import com.radkevich.Messengers.model.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public abstract class FileSaver {

    public void saveFile() throws IOException{
    };

}
