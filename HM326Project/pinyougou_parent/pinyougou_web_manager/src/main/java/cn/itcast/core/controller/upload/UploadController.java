package cn.itcast.core.controller.upload;

import cn.itcast.core.entity.Result;
import cn.itcast.core.utils.fdfs.FastDFSClient;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("upload")
public class UploadController {

    @Value("${FILE_SERVER_URL}")
    private String FILE_SERVER_URL;

    @RequestMapping("uploadFile")
    public Result uploadFile(MultipartFile file) {
        try {
            String conf="classpath:fastDFS/fdfs_client.conf";
            FastDFSClient fastDFSClient=new FastDFSClient(conf);
            String originalFilename = file.getOriginalFilename();
            String extName = FilenameUtils.getExtension(originalFilename);
            String path = fastDFSClient.uploadFile(file.getBytes(), extName,null);
            String url= FILE_SERVER_URL + path;
            return new Result(true,url);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"上传失败");
        }
    }
}
