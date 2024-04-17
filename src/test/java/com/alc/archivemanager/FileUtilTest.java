package com.alc.archivemanager;

import com.alc.archivemanager.util.FileUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

@SpringBootTest
public class FileUtilTest {

    private final String TRASH_FOLDER = "C:/WebPractice/archive-manager/src/main/resources/TrashFolder";
    private final String MAIN_PATH = "C:/WebPractice/archive-manager/src/main/resources/storage";

    @Test
    public void moveToTrashTest() throws IOException {
        File file = new File(MAIN_PATH, "testFile");
        file.createNewFile();

        FileUtil.moveToTrashFile(file);
        File trashed = new File(TRASH_FOLDER, "testFile");

        assertThat(!file.exists() && trashed.exists()).isTrue();
        trashed.delete();
        (new File(TRASH_FOLDER, trashed.getName() + ".metadata")).delete();
    }


    @Test
    public void recoverFileTest() throws IOException {
        File file = new File(MAIN_PATH, "testFile");
        file.createNewFile();

        FileUtil.moveToTrashFile(file);
        File trashed = new File(TRASH_FOLDER, "testFile");

        FileUtil.recoverFile(trashed);

        assertThat(file.exists() && !trashed.exists()).isTrue();
        file.delete();
    }
}
