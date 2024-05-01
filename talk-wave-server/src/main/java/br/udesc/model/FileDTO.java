package br.udesc.model;

public class FileDTO {

    private String fileName;
    private String base64;

    public FileDTO(String fileName, String base64) {
        this.fileName = fileName;
        this.base64 = base64;
    }

    public String getFileName() {
        return fileName;
    }

    public String getBase64() {
        return base64;
    }
}
