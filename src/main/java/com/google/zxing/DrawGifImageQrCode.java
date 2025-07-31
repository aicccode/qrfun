package com.google.zxing;

import com.alibaba.fastjson2.JSONArray;
import lombok.extern.slf4j.Slf4j;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
public final class DrawGifImageQrCode extends DrawQrCode {
  public static void drawGifImage(
      boolean idDotBlack,
      String outpath,
      String content,
      int width,
      int height,
      int x,
      int y,
      JSONArray jbg,
      JSONArray jeye,
      JSONArray j1x1,
      JSONArray j1x2,
      JSONArray j1x3,
      JSONArray j1x4,
      JSONArray j2x1,
      JSONArray j2x2,
      JSONArray j2x3,
      JSONArray j3x1,
      JSONArray j3x2,
      JSONArray j3x3,
      JSONArray j4x1) {
    Map<String, ArrayList<String>> elements = new HashMap<>();
    ArrayList<String> ebg = new ArrayList<>();
    for (int i = 0; i < jbg.size(); i++) {
      ebg.add(jbg.getString(i));
    }
    elements.put(JBG, ebg);
    ArrayList<String> eeye = new ArrayList<>();
    for (int i = 0; i < jeye.size(); i++) {
      eeye.add(jeye.getString(i));
    }
    elements.put(JEYE, eeye);
    ArrayList<String> e1x1 = new ArrayList<>();
    for (int i = 0; i < j1x1.size(); i++) {
      e1x1.add(j1x1.getString(i));
    }
    elements.put(J1X1, e1x1);
    ArrayList<String> e1x2 = new ArrayList<>();
    for (int i = 0; i < j1x2.size(); i++) {
      e1x2.add(j1x2.getString(i));
    }
    elements.put(J1X2, e1x2);
    ArrayList<String> e1x3 = new ArrayList<>();
    for (int i = 0; i < j1x3.size(); i++) {
      e1x3.add(j1x3.getString(i));
    }
    elements.put(J1X3, e1x3);
    ArrayList<String> e1x4 = new ArrayList<>();
    for (int i = 0; i < j1x4.size(); i++) {
      e1x4.add(j1x4.getString(i));
    }
    elements.put(J1X4, e1x4);
    ArrayList<String> e2x1 = new ArrayList<>();
    for (int i = 0; i < j2x1.size(); i++) {
      e2x1.add(j2x1.getString(i));
    }
    elements.put(J2X1, e2x1);
    ArrayList<String> e2x2 = new ArrayList<>();
    for (int i = 0; i < j2x2.size(); i++) {
      e2x2.add(j2x2.getString(i));
    }
    elements.put(J2X2, e2x2);
    ArrayList<String> e2x3 = new ArrayList<>();
    for (int i = 0; i < j2x3.size(); i++) {
      e2x3.add(j2x3.getString(i));
    }
    elements.put(J2X3, e2x3);
    ArrayList<String> e3x1 = new ArrayList<>();
    for (int i = 0; i < j3x1.size(); i++) {
      e3x1.add(j3x1.getString(i));
    }
    elements.put(J3X1, e3x1);
    ArrayList<String> e3x2 = new ArrayList<>();
    for (int i = 0; i < j3x2.size(); i++) {
      e3x2.add(j3x2.getString(i));
    }
    elements.put(J3X2, e3x2);
    ArrayList<String> e3x3 = new ArrayList<>();
    for (int i = 0; i < j3x3.size(); i++) {
      e3x3.add(j3x3.getString(i));
    }
    elements.put(J3X3, e3x3);
    ArrayList<String> e4x1 = new ArrayList<>();
    for (int i = 0; i < j4x1.size(); i++) {
      e4x1.add(j4x1.getString(i));
    }
    elements.put(J4X1, e4x1);
    String seid = UUID.randomUUID().toString();
    qrpadding.put(seid, new HashMap<>());
    drawQrCode(seid, content, idDotBlack, elements, width, height, x, y, outpath);
  }

  private static void drawQrCode(
      String seid,
      String content,
      boolean idDotBlack,
      Map<String, ArrayList<String>> elements,
      int width,
      int height,
      int x,
      int y,
      String outPath) {
    BufferedImage qr = drawQR(seid, elements, idDotBlack, content, 50);
    if (qr != null) {
      qr =
          qr.getSubimage(
              qrpadding.get(seid).get(LEFT) - 50,
              qrpadding.get(seid).get(TOP) - 50,
              qr.getWidth() - 2 * qrpadding.get(seid).get(LEFT) + 2 * 50,
              qr.getHeight() - 2 * qrpadding.get(seid).get(TOP) + 2 * 50);
      try {
        qr = resize(qr, width, height, true);
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.setRepeat(0);
        encoder.start(outPath);
        encoder.setFrameRate(12);
        GifDecoder decoder = new GifDecoder();
        decoder.read(elements.get(JBG).get(0));
        for (int i = 0; i < decoder.getFrameCount(); i++) {
          BufferedImage image = decoder.getFrame(i);
          Graphics2D gbg = image.createGraphics();
          gbg.drawImage(qr, x, y, width, height, null, null);
          gbg.dispose();
          encoder.addFrame(image);
        }
        encoder.finish();
      } catch (Exception e) {
        log.error(e.getMessage());
      }
    }
  }

  public static BufferedImage drawQR(
      String seid,
      Map<String, ArrayList<String>> elements,
      boolean idDotBlack,
      String content,
      int dotpix) {
    boolean versionerror = true;
    int v = 1;
    while (versionerror) {
      try {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.QR_VERSION, v);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        int line = (((Integer) hints.get(EncodeHintType.QR_VERSION) - 1) * 4 + 21 + 8) * dotpix;
        BitMatrix matrix =
            new QRCodeWriter().encode(seid, content, BarcodeFormat.QR_CODE, line, line, hints);
        BufferedImage image = toBufferedImage(elements, idDotBlack, matrix, dotpix);
        System.out.println("v is :" + v);
        versionerror = false;
        return image;
      } catch (IOException | WriterException e) {
        if (e.getMessage().equals("Data too big for requested version")) {
          v++;
        } else {
          versionerror = false;
          log.error(e.getMessage());
        }
      }
    }
    return null;
  }

  public static BufferedImage toBufferedImage(
      Map<String, ArrayList<String>> elements, boolean idDotBlack, BitMatrix matrix, int dotpix)
      throws IOException {
    int width = matrix.getWidth();
    int height = matrix.getHeight();
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics2D gbg = image.createGraphics();
    image =
        gbg.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
    gbg.dispose();
    Graphics2D g = image.createGraphics();
    Map<String, Boolean> b = new HashMap<>();
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if (x % dotpix == 0 && y % dotpix == 0) {
          if (idDotBlack && matrix.get(x, y) && isNotTag(true, x, y, width, height)) {
            b.put(x + ":" + y, Boolean.TRUE);
          } else if (!idDotBlack
              && !matrix.get(x, y)
              && isNotTag(false, x, y, width, height)) {
            b.put(x + ":" + y, Boolean.TRUE);
          }
        }
      }
    }
    int si = 3 - elements.get(JEYE).size();
    for (int i = 0; i < si; i++) {
      elements.get(JEYE).add(elements.get(JEYE).get(0));
    }
    if (idDotBlack) {
      drawBlackElements(elements, height, width, dotpix, matrix, b, g);
      BufferedImage tag0 = ImageIO.read(new File(elements.get(JEYE).get(0)));
      g.drawImage(tag0, dotpix * 4, dotpix * 4, null, null);
      BufferedImage tag1 = ImageIO.read(new File(elements.get(JEYE).get(1)));
      g.drawImage(tag1, width - tag1.getWidth() - dotpix * 4, dotpix * 4, null, null);
      BufferedImage tag2 = ImageIO.read(new File(elements.get(JEYE).get(2)));
      g.drawImage(tag2, dotpix * 4, height - tag2.getHeight() - dotpix * 4, null, null);
    } else {
      drawWhiteElements(elements, height, width, dotpix, matrix, b, g);
      BufferedImage tag0 = ImageIO.read(new File(elements.get(JEYE).get(0)));
      g.drawImage(tag0, dotpix * 3, dotpix * 3, null, null);
      BufferedImage tag1 = ImageIO.read(new File(elements.get(JEYE).get(1)));
      g.drawImage(tag1, width - tag1.getWidth() - dotpix * 3, dotpix * 3, null, null);
      BufferedImage tag2 = ImageIO.read(new File(elements.get(JEYE).get(2)));
      g.drawImage(tag2, dotpix * 3, height - tag2.getHeight() - dotpix * 3, null, null);
    }
    g.dispose();
    return image;
  }
}
