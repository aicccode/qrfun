package aicc.site.gen;

import ch.qos.logback.core.encoder.ByteArrayUtil;
import com.alibaba.fastjson2.JSONArray;
import com.google.zxing.DrawGifImageQrCode;
import com.google.zxing.DrawPngImageQrCode;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Generator {
  // replace your workspace path
  private static final String basePath = "/home/aicccode/workspace/qrfun/src/main/resources";

  private static JSONArray resetImageRealPath(String url) {
    JSONArray array = JSONArray.parse(url);
    for (int i = 0; i < array.size(); i++) {
      String s = array.getString(i);
      array.set(i, basePath + s);
    }
    return array;
  }

  public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
    List<Module> modules = readModuleFromJson();
    String content = "Hello this is Qrfun for you!";
    String outPutPath = "/home/aicccode/output";
    genByModule(modules.get(0), content, outPutPath);
  }

  private static List<Module> readModuleFromJson() throws IOException {
    try (InputStream reader = Files.newInputStream(Paths.get(basePath + "/module.json"))) {
      byte[] bytes = new byte[reader.available()];
      int l = reader.read(bytes);
      if (l == bytes.length) {
        String json = new String(bytes);
        return JSONArray.parseArray(json, Module.class);
      } else {
        throw new RuntimeException("read module from json failed.");
      }
    }
  }

  private static void genByModule(Module module, String content, String outPath)
      throws NoSuchAlgorithmException {
    String bgType = module.getBgtype();
    String qrType = module.getQrtype();
    MessageDigest md = MessageDigest.getInstance("MD5");
    String fileName =
        "qrfun_"
            + ByteArrayUtil.toHexString(md.digest(content.getBytes(StandardCharsets.UTF_8)))
            + ("02".equals(bgType) ? ".gif" : ".png");
    String fullPath = outPath + fileName;
    File file = new File(fullPath);
    if ("02".equals(bgType)) {
      if (!file.exists()) {
        DrawGifImageQrCode.drawGifImage(
            "01".equals(qrType),
            fullPath,
            content,
            module.getQrwidth(),
            module.getQrheight(),
            module.getPox(),
            module.getPoy(),
            resetImageRealPath(module.getBgurl()),
            resetImageRealPath(module.getQreyeurl()),
            resetImageRealPath(module.getQr1x1url()),
            resetImageRealPath(module.getQr1x2url()),
            resetImageRealPath(module.getQr1x3url()),
            resetImageRealPath(module.getQr1x4url()),
            resetImageRealPath(module.getQr2x1url()),
            resetImageRealPath(module.getQr2x2url()),
            resetImageRealPath(module.getQr2x3url()),
            resetImageRealPath(module.getQr3x1url()),
            resetImageRealPath(module.getQr3x2url()),
            resetImageRealPath(module.getQr3x3url()),
            resetImageRealPath(module.getQr4x1url()));
      }
    } else {
      if (!file.exists()) {
        DrawPngImageQrCode.drawPngImage(
            "01".equals(qrType),
            fullPath,
            content,
            module.getQrwidth(),
            module.getQrheight(),
            module.getPox(),
            module.getPoy(),
            resetImageRealPath(module.getBgurl()),
            resetImageRealPath(module.getQreyeurl()),
            resetImageRealPath(module.getQr1x1url()),
            resetImageRealPath(module.getQr1x2url()),
            resetImageRealPath(module.getQr1x3url()),
            resetImageRealPath(module.getQr1x4url()),
            resetImageRealPath(module.getQr2x1url()),
            resetImageRealPath(module.getQr2x2url()),
            resetImageRealPath(module.getQr2x3url()),
            resetImageRealPath(module.getQr3x1url()),
            resetImageRealPath(module.getQr3x2url()),
            resetImageRealPath(module.getQr3x3url()),
            resetImageRealPath(module.getQr4x1url()));
      }
    }
  }
}
