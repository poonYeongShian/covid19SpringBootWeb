package com.example.servingwebcontent.tool;

import java.io.IOException;
import java.nio.file.Paths;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

public class MyQr {

    private String data;
    private String path;

    public MyQr(String data, String path) {
        this.data = data;
        this.path = path;
    }
    public void generateQR() throws WriterException, IOException {
//        String data = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
//        String path = "C:\\Users\\User\\Desktop\\new\\heheboi.jpg";

        BitMatrix matrix = new MultiFormatWriter()
                .encode(data, BarcodeFormat.QR_CODE, 500, 500);

        MatrixToImageWriter.writeToPath(matrix, "jpg", Paths.get(path));
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
