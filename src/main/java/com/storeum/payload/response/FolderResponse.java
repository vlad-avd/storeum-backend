package com.storeum.payload.response;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class FolderResponse {

    private Long id;
    private String title;
    private Long parentFolderId;
    private List<FolderResponse> subFolders;
    private List<TagResponse> tags;


}
