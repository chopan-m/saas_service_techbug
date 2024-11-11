package com.user_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FileResponse {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
}
