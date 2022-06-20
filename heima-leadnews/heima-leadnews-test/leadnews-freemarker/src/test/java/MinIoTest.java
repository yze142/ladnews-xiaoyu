import io.minio.MinioClient;
import io.minio.PutObjectArgs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @program: heima-leadnews
 * @description:
 * @author: hello.xaioyu
 * @create: 2022-06-04 23:32
 **/

public class MinIoTest {

    public static void main(String[] args) {

        try {
            FileInputStream fileInputStream=new FileInputStream("D:\\java文件\\javaV12\\09阶段九：项目实战-黑马头条(V12.5)\\02-资料\\day02-app端文章查看，静态化freemarker,分布式文件系统minIO\\资料\\模板文件\\plugins\\js\\index.js");

            //获取minio客户移动端的链接信息
            MinioClient client=MinioClient.builder()
                    .credentials("minio","minio123")//客户端账户名字和密码
            .endpoint("http://192.168.200.130:9000").build();//客户端的请求的路劲

            //上传操作
            PutObjectArgs leadnews = PutObjectArgs.builder().object("plugins/js/index.js")//文件名称
                    .contentType("text/js")//文件类型
                    .bucket("leadnews")//桶名称
                    .stream(fileInputStream, fileInputStream.available(), -1).build();//意思大概就是上传所有把

              client.putObject(leadnews);
            //获取访问路径
            System.out.println(leadnews.genHeaders().toString());








        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
