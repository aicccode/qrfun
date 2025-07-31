package com.google.zxing;

import com.google.zxing.common.BitMatrix;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;

public class DrawQrCode {
  public static Map<String, HashMap<String, Integer>> qrpadding =
      Collections.synchronizedMap(new HashMap<>());
  public static final String J1X1 = "J1X1";
  public static final String J1X2 = "J1X2";
  public static final String J1X3 = "J1X3";
  public static final String J1X4 = "J1X4";
  public static final String J2X1 = "J2X1";
  public static final String J2X2 = "J2X2";
  public static final String J2X3 = "J2X3";
  public static final String J3X1 = "J3X1";
  public static final String J3X2 = "J3X2";
  public static final String J3X3 = "J3X3";
  public static final String J4X1 = "J4X1";
  public static final String JEYE = "JEYE";
  public static final String JBG = "JBG";

  public static final String LEFT = "LEFT";
  public static final String TOP = "TOP";

  // 判断当前区域是否为tag区
  public static boolean isNotTag(boolean idDotBlack, int x, int y, int width, int height) {
    if (idDotBlack) {
      return (x > 550 || y > 550) && (x > 550 || y < height - 550) && (y > 550 || x < width - 550);
    } else {
      return (x > 550 || y > 550) && (x > 550 || y < height - 600) && (y > 550 || x < width - 600);
    }
  }

  public static BufferedImage resize(
      BufferedImage source, int targetW, int targetH, boolean equalProportion) {
    int type = source.getType();
    BufferedImage target;
    double sx = (double) targetW / source.getWidth();
    double sy = (double) targetH / source.getHeight();
    if (equalProportion) {
      if (sx > sy) {
        sx = sy;
        targetW = (int) (sx * source.getWidth());
      } else {
        sy = sx;
        targetH = (int) (sx * source.getHeight());
      }
    }
    if (type == BufferedImage.TYPE_CUSTOM) {
      ColorModel cm = source.getColorModel();
      WritableRaster raster = cm.createCompatibleWritableRaster(targetW, targetH);
      boolean alphaPremultiplied = cm.isAlphaPremultiplied();
      target = new BufferedImage(cm, raster, alphaPremultiplied, null);
    } else {
      target = new BufferedImage(targetW, targetH, type);
      Graphics2D g = target.createGraphics();
      g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
      g.dispose();
    }
    return target;
  }

  public static void drawWhiteElements(
      Map<String, ArrayList<String>> elements,
      int height,
      int width,
      int dotpix,
      BitMatrix matrix,
      Map<String, Boolean> b,
      Graphics2D g)
      throws IOException {
    Random random = new Random();
    // 4x1
    if (elements.containsKey(J4X1) && !elements.get(J4X1).isEmpty()) {
      for (int y = 200; y < height - 250; y++) {
        for (int x = 200; x < width - 250; x++) {
          if (x % dotpix == 0 && y % dotpix == 0) {
            if ((x + 3 * dotpix < width)
                && !matrix.get(x, y)
                && !matrix.get(x + dotpix, y)
                && !matrix.get(x + 2 * dotpix, y)
                && !matrix.get(x + 3 * dotpix, y)
                && b.containsKey(x + ":" + y)
                && b.containsKey((x + dotpix) + ":" + y)
                && b.containsKey((x + 2 * dotpix) + ":" + y)
                && b.containsKey((x + 3 * dotpix) + ":" + y)) {

              int k = random.nextInt((elements.get(J4X1).size()));
              g.drawImage(ImageIO.read(new File(elements.get(J4X1).get(k))), x, y, null, null);
              b.remove(x + ":" + y);
              b.remove((x + dotpix) + ":" + y);
              b.remove((x + 2 * dotpix) + ":" + y);
              b.remove((x + 3 * dotpix) + ":" + y);
            }
          }
        }
      }
    }
    // 1x4
    if (elements.containsKey(J1X4) && !elements.get(J1X4).isEmpty()) {
      for (int y = 200; y < height - 250; y++) {
        for (int x = 200; x < width - 250; x++) {
          if (x % dotpix == 0 && y % dotpix == 0) {
            if (((y + 3 * dotpix) < height)
                && !matrix.get(x, y)
                && !matrix.get(x, y + dotpix)
                && !matrix.get(x, y + 2 * dotpix)
                && !matrix.get(x, y + 3 * dotpix)
                && b.containsKey(x + ":" + y)
                && b.containsKey(x + ":" + (y + dotpix))
                && b.containsKey(x + ":" + (y + 2 * dotpix))
                && b.containsKey(x + ":" + (y + 3 * dotpix))) {

              int k = random.nextInt((elements.get(J1X4).size()));
              g.drawImage(ImageIO.read(new File(elements.get(J1X4).get(k))), x, y, null, null);
              b.remove(x + ":" + y);
              b.remove(x + ":" + (y + dotpix));
              b.remove(x + ":" + (y + 2 * dotpix));
              b.remove(x + ":" + (y + 3 * dotpix));
            }
          }
        }
      }
    }
    // 3x3
    if (elements.containsKey(J3X3) && !elements.get(J3X3).isEmpty()) {
      for (int y = 200; y < height - 250; y++) {
        for (int x = 200; x < width - 250; x++) {
          if (x % dotpix == 0 && y % dotpix == 0) {
            if (((y + 2 * dotpix) < height)
                && ((x + 2 * dotpix) < width)
                && !matrix.get(x, y)
                && !matrix.get(x, y + dotpix)
                && !matrix.get(x, y + 2 * dotpix)
                && !matrix.get(x + dotpix, y)
                && !matrix.get(x + dotpix, y + dotpix)
                && !matrix.get(x + dotpix, y + 2 * dotpix)
                && !matrix.get(x + 2 * dotpix, y)
                && !matrix.get(x + 2 * dotpix, y + dotpix)
                && !matrix.get(x + 2 * dotpix, y + 2 * dotpix)
                && b.containsKey(x + ":" + y)
                && b.containsKey(x + ":" + (y + dotpix))
                && b.containsKey(x + ":" + (y + 2 * dotpix))
                && b.containsKey((x + dotpix) + ":" + y)
                && b.containsKey((x + dotpix) + ":" + (y + dotpix))
                && b.containsKey((x + dotpix) + ":" + (y + 2 * dotpix))
                && b.containsKey((x + 2 * dotpix) + ":" + y)
                && b.containsKey((x + 2 * dotpix) + ":" + (y + dotpix))
                && b.containsKey((x + 2 * dotpix) + ":" + (y + 2 * dotpix))) {
              int k = random.nextInt((elements.get(J3X3).size()));
              g.drawImage(ImageIO.read(new File(elements.get(J3X3).get(k))), x, y, null, null);
              b.remove(x + ":" + y);
              b.remove(x + ":" + (y + dotpix));
              b.remove(x + ":" + (y + 2 * dotpix));
              b.remove((x + dotpix) + ":" + y);
              b.remove((x + dotpix) + ":" + (y + dotpix));
              b.remove((x + dotpix) + ":" + (y + 2 * dotpix));
              b.remove((x + 2 * dotpix) + ":" + y);
              b.remove((x + 2 * dotpix) + ":" + (y + dotpix));
              b.remove((x + 2 * dotpix) + ":" + (y + 2 * dotpix));
            }
          }
        }
      }
    }
    // 3x2
    if (elements.containsKey(J3X2) && !elements.get(J3X2).isEmpty()) {
      for (int y = 200; y < height - 250; y++) {
        for (int x = 200; x < width - 250; x++) {
          if (x % dotpix == 0 && y % dotpix == 0) {
            if (((x + 2 * dotpix) < width)
                && ((y + dotpix) < height)
                && !matrix.get(x, y)
                && !matrix.get(x, y + dotpix)
                && !matrix.get(x + dotpix, y)
                && !matrix.get(x + dotpix, y + dotpix)
                && !matrix.get(x + 2 * dotpix, y)
                && !matrix.get(x + 2 * dotpix, y + dotpix)
                && b.containsKey(x + ":" + y)
                && b.containsKey(x + ":" + (y + dotpix))
                && b.containsKey((x + dotpix) + ":" + y)
                && b.containsKey((x + dotpix) + ":" + (y + dotpix))
                && b.containsKey((x + 2 * dotpix) + ":" + y)
                && b.containsKey((x + 2 * dotpix) + ":" + (y + dotpix))) {
              int k = random.nextInt((elements.get(J3X2).size()));
              g.drawImage(ImageIO.read(new File(elements.get(J3X2).get(k))), x, y, null, null);
              b.remove(x + ":" + y);
              b.remove(x + ":" + (y + dotpix));
              b.remove((x + dotpix) + ":" + y);
              b.remove((x + dotpix) + ":" + (y + dotpix));
              b.remove((x + 2 * dotpix) + ":" + y);
              b.remove((x + 2 * dotpix) + ":" + (y + dotpix));
            }
          }
        }
      }
    }
    // 3x1
    if (elements.containsKey(J3X1) && !elements.get(J3X1).isEmpty()) {
      for (int y = 200; y < height - 250; y++) {
        for (int x = 200; x < width - 250; x++) {
          if (x % dotpix == 0 && y % dotpix == 0) {
            if (((x + 2 * dotpix) < width)
                && !matrix.get(x, y)
                && !matrix.get(x + dotpix, y)
                && !matrix.get(x + 2 * dotpix, y)
                && b.containsKey(x + ":" + y)
                && b.containsKey((x + dotpix) + ":" + y)
                && b.containsKey((x + 2 * dotpix) + ":" + y)) {
              int k = random.nextInt((elements.get(J3X1).size()));
              g.drawImage(ImageIO.read(new File(elements.get(J3X1).get(k))), x, y, null, null);
              b.remove(x + ":" + y);
              b.remove((x + dotpix) + ":" + y);
              b.remove((x + 2 * dotpix) + ":" + y);
            }
          }
        }
      }
    }
    // 2x3
    if (elements.containsKey(J2X3) && !elements.get(J2X3).isEmpty()) {
      for (int y = 200; y < height - 250; y++) {
        for (int x = 200; x < width - 250; x++) {
          if (x % dotpix == 0 && y % dotpix == 0) {
            if (((x + dotpix) < width)
                && ((y + 2 * dotpix) < height)
                && !matrix.get(x, y)
                && !matrix.get(x, y + dotpix)
                && !matrix.get(x, y + 2 * dotpix)
                && !matrix.get(x + dotpix, y)
                && !matrix.get(x + dotpix, y + dotpix)
                && !matrix.get(x + dotpix, y + 2 * dotpix)
                && b.containsKey(x + ":" + y)
                && b.containsKey(x + ":" + (y + dotpix))
                && b.containsKey(x + ":" + (y + 2 * dotpix))
                && b.containsKey((x + dotpix) + ":" + y)
                && b.containsKey((x + dotpix) + ":" + (y + dotpix))
                && b.containsKey((x + dotpix) + ":" + (y + 2 * dotpix))) {
              int k = random.nextInt((elements.get(J2X3).size()));
              g.drawImage(ImageIO.read(new File(elements.get(J2X3).get(k))), x, y, null, null);
              b.remove(x + ":" + y);
              b.remove(x + ":" + (y + dotpix));
              b.remove(x + ":" + (y + 2 * dotpix));
              b.remove((x + dotpix) + ":" + y);
              b.remove((x + dotpix) + ":" + (y + dotpix));
              b.remove((x + dotpix) + ":" + (y + 2 * dotpix));
            }
          }
        }
      }
    }
    // 1x3
    if (elements.containsKey(J1X3) && !elements.get(J1X3).isEmpty()) {
      for (int y = 200; y < height - 250; y++) {
        for (int x = 200; x < width - 250; x++) {
          if (x % dotpix == 0 && y % dotpix == 0) {
            if (((y + 2 * dotpix) < height)
                && !matrix.get(x, y)
                && !matrix.get(x, y + dotpix)
                && !matrix.get(x, y + 2 * dotpix)
                && b.containsKey(x + ":" + y)
                && b.containsKey(x + ":" + (y + dotpix))
                && b.containsKey(x + ":" + (y + 2 * dotpix))) {

              int k = random.nextInt((elements.get(J1X3).size()));
              g.drawImage(ImageIO.read(new File(elements.get(J1X3).get(k))), x, y, null, null);
              b.remove(x + ":" + y);
              b.remove(x + ":" + (y + dotpix));
              b.remove(x + ":" + (y + 2 * dotpix));
            }
          }
        }
      }
    }

    // 2x2
    if (elements.containsKey(J2X2) && !elements.get(J2X2).isEmpty()) {
      for (int y = 200; y < height - 250; y++) {
        for (int x = 200; x < width - 250; x++) {
          if (x % dotpix == 0 && y % dotpix == 0) {
            if (((x + dotpix) < width)
                && ((y + dotpix) < height)
                && !matrix.get(x, y)
                && !matrix.get(x, y + dotpix)
                && !matrix.get(x + dotpix, y)
                && !matrix.get(x + dotpix, y + dotpix)
                && b.containsKey(x + ":" + y)
                && b.containsKey(x + ":" + (y + dotpix))
                && b.containsKey((x + dotpix) + ":" + y)
                && b.containsKey((x + dotpix) + ":" + (y + dotpix))) {
              int k = random.nextInt((elements.get(J2X2).size()));
              g.drawImage(ImageIO.read(new File(elements.get(J2X2).get(k))), x, y, null, null);
              b.remove(x + ":" + y);
              b.remove(x + ":" + (y + dotpix));
              b.remove((x + dotpix) + ":" + y);
              b.remove((x + dotpix) + ":" + (y + dotpix));
            }
          }
        }
      }
    }
    // 2x1
    if (elements.containsKey(J2X1) && !elements.get(J2X1).isEmpty()) {
      for (int y = 200; y < height - 250; y++) {
        for (int x = 200; x < width - 250; x++) {
          if (x % dotpix == 0 && y % dotpix == 0) {
            if (((x + dotpix) < width)
                && !matrix.get(x, y)
                && !matrix.get(x + dotpix, y)
                && b.containsKey(x + ":" + y)
                && b.containsKey((x + dotpix) + ":" + y)) {
              int k = random.nextInt((elements.get(J2X1).size()));
              g.drawImage(ImageIO.read(new File(elements.get(J2X1).get(k))), x, y, null, null);
              b.remove(x + ":" + y);
              b.remove((x + dotpix) + ":" + y);
            }
          }
        }
      }
    }

    // 1x2
    if (elements.containsKey(J1X2) && !elements.get(J1X2).isEmpty()) {
      for (int y = 200; y < height - 250; y++) {
        for (int x = 200; x < width - 250; x++) {
          if (x % dotpix == 0 && y % dotpix == 0) {
            if (((y + dotpix) < height)
                && !matrix.get(x, y)
                && !matrix.get(x, y + dotpix)
                && b.containsKey(x + ":" + y)
                && b.containsKey(x + ":" + (y + dotpix))) {

              int k = random.nextInt((elements.get(J1X2).size()));
              g.drawImage(ImageIO.read(new File(elements.get(J1X2).get(k))), x, y, null, null);
              b.remove(x + ":" + y);
              b.remove(x + ":" + (y + dotpix));
            }
          }
        }
      }
    }
    // 1x1
    if (elements.containsKey(J1X1) && !elements.get(J1X1).isEmpty()) {
      for (int y = 150; y < height - 199; y++) {
        for (int x = 150; x < width - 199; x++) {
          if (x % dotpix == 0 && y % dotpix == 0) {
            if (!matrix.get(x, y) && b.containsKey(x + ":" + y)) {
              int k = random.nextInt((elements.get(J1X1).size()));
              g.drawImage(ImageIO.read(new File(elements.get(J1X1).get(k))), x, y, null, null);
              b.remove(x + ":" + y);
            }
          }
        }
      }
    }
  }

  public static void drawBlackElements(
      Map<String, ArrayList<String>> elements,
      int height,
      int width,
      int dotpix,
      BitMatrix matrix,
      Map<String, Boolean> b,
      Graphics2D g)
      throws IOException {
    Random random = new Random();
    // 4x1
    if (elements.containsKey(J4X1) && !elements.get(J4X1).isEmpty()) {
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          if (x % dotpix == 0 && y % dotpix == 0) {
            if (((x + 3 * dotpix) < width)
                && matrix.get(x, y)
                && matrix.get(x + dotpix, y)
                && matrix.get(x + 2 * dotpix, y)
                && matrix.get(x + 3 * dotpix, y)
                && b.containsKey(x + ":" + y)
                && b.containsKey((x + dotpix) + ":" + y)
                && b.containsKey((x + 2 * dotpix) + ":" + y)
                && b.containsKey((x + 3 * dotpix) + ":" + y)) {

              int k = random.nextInt((elements.get(J4X1).size()));
              g.drawImage(ImageIO.read(new File(elements.get(J4X1).get(k))), x, y, null, null);
              b.remove(x + ":" + y);
              b.remove((x + dotpix) + ":" + y);
              b.remove((x + 2 * dotpix) + ":" + y);
              b.remove((x + 3 * dotpix) + ":" + y);
            }
          }
        }
      }
    }
    // 1x4
    if (elements.containsKey(J1X4) && !elements.get(J1X4).isEmpty()) {
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          if (x % dotpix == 0 && y % dotpix == 0) {
            if (((y + 3 * dotpix) < height)
                && matrix.get(x, y)
                && matrix.get(x, y + dotpix)
                && matrix.get(x, y + 2 * dotpix)
                && matrix.get(x, y + 3 * dotpix)
                && b.containsKey(x + ":" + y)
                && b.containsKey(x + ":" + (y + dotpix))
                && b.containsKey(x + ":" + (y + 2 * dotpix))
                && b.containsKey(x + ":" + (y + 3 * dotpix))) {

              int k = random.nextInt((elements.get(J1X4).size()));
              g.drawImage(ImageIO.read(new File(elements.get(J1X4).get(k))), x, y, null, null);
              b.remove(x + ":" + y);
              b.remove(x + ":" + (y + dotpix));
              b.remove(x + ":" + (y + 2 * dotpix));
              b.remove(x + ":" + (y + 3 * dotpix));
            }
          }
        }
      }
    }
    // 3x3
    if (elements.containsKey(J3X3) && !elements.get(J3X3).isEmpty()) {
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          if (x % dotpix == 0 && y % dotpix == 0) {
            if (((x + 2 * dotpix) < width)
                && ((y + 2 * dotpix) < height)
                && matrix.get(x, y)
                && matrix.get(x, y + dotpix)
                && matrix.get(x, y + 2 * dotpix)
                && matrix.get(x + dotpix, y)
                && matrix.get(x + dotpix, y + dotpix)
                && matrix.get(x + dotpix, y + 2 * dotpix)
                && matrix.get(x + 2 * dotpix, y)
                && matrix.get(x + 2 * dotpix, y + dotpix)
                && matrix.get(x + 2 * dotpix, y + 2 * dotpix)
                && b.containsKey(x + ":" + y)
                && b.containsKey(x + ":" + (y + dotpix))
                && b.containsKey(x + ":" + (y + 2 * dotpix))
                && b.containsKey((x + dotpix) + ":" + y)
                && b.containsKey((x + dotpix) + ":" + (y + dotpix))
                && b.containsKey((x + dotpix) + ":" + (y + 2 * dotpix))
                && b.containsKey((x + 2 * dotpix) + ":" + y)
                && b.containsKey((x + 2 * dotpix) + ":" + (y + dotpix))
                && b.containsKey((x + 2 * dotpix) + ":" + (y + 2 * dotpix))) {
              int k = random.nextInt((elements.get(J3X3).size()));
              g.drawImage(ImageIO.read(new File(elements.get(J3X3).get(k))), x, y, null, null);
              b.remove(x + ":" + y);
              b.remove(x + ":" + (y + dotpix));
              b.remove(x + ":" + (y + 2 * dotpix));
              b.remove((x + dotpix) + ":" + y);
              b.remove((x + dotpix) + ":" + (y + dotpix));
              b.remove((x + dotpix) + ":" + (y + 2 * dotpix));
              b.remove((x + 2 * dotpix) + ":" + y);
              b.remove((x + 2 * dotpix) + ":" + (y + dotpix));
              b.remove((x + 2 * dotpix) + ":" + (y + 2 * dotpix));
            }
          }
        }
      }
    }
    // 3x2
    if (elements.containsKey(J3X2) && !elements.get(J3X2).isEmpty()) {
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          if (x % dotpix == 0 && y % dotpix == 0) {
            if (((x + 2 * dotpix) < width)
                && ((y + dotpix) < height)
                && matrix.get(x, y)
                && matrix.get(x, y + dotpix)
                && matrix.get(x + dotpix, y)
                && matrix.get(x + dotpix, y + dotpix)
                && matrix.get(x + 2 * dotpix, y)
                && matrix.get(x + 2 * dotpix, y + dotpix)
                && b.containsKey(x + ":" + y)
                && b.containsKey(x + ":" + (y + dotpix))
                && b.containsKey((x + dotpix) + ":" + y)
                && b.containsKey((x + dotpix) + ":" + (y + dotpix))
                && b.containsKey((x + 2 * dotpix) + ":" + y)
                && b.containsKey((x + 2 * dotpix) + ":" + (y + dotpix))) {
              int k = random.nextInt((elements.get(J3X2).size()));
              g.drawImage(ImageIO.read(new File(elements.get(J3X2).get(k))), x, y, null, null);
              b.remove(x + ":" + y);
              b.remove(x + ":" + (y + dotpix));
              b.remove((x + dotpix) + ":" + y);
              b.remove((x + dotpix) + ":" + (y + dotpix));
              b.remove((x + 2 * dotpix) + ":" + y);
              b.remove((x + 2 * dotpix) + ":" + (y + dotpix));
            }
          }
        }
      }
    }
    // 3x1
    if (elements.containsKey(J3X1) && !elements.get(J3X1).isEmpty()) {
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          if (x % dotpix == 0 && y % dotpix == 0) {
            if (((x + 2 * dotpix) < width)
                && matrix.get(x, y)
                && matrix.get(x + dotpix, y)
                && matrix.get(x + 2 * dotpix, y)
                && b.containsKey(x + ":" + y)
                && b.containsKey((x + dotpix) + ":" + y)
                && b.containsKey((x + 2 * dotpix) + ":" + y)) {
              int k = random.nextInt((elements.get(J3X1).size()));
              g.drawImage(ImageIO.read(new File(elements.get(J3X1).get(k))), x, y, null, null);
              b.remove(x + ":" + y);
              b.remove((x + dotpix) + ":" + y);
              b.remove((x + 2 * dotpix) + ":" + y);
            }
          }
        }
      }
    }
    // 2x3
    if (elements.containsKey(J2X3) && !elements.get(J2X3).isEmpty()) {
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          if (x % dotpix == 0 && y % dotpix == 0) {
            if (((x + dotpix) < width)
                && ((y + 2 * dotpix) < height)
                && matrix.get(x, y)
                && matrix.get(x, y + dotpix)
                && matrix.get(x, y + 2 * dotpix)
                && matrix.get(x + dotpix, y)
                && matrix.get(x + dotpix, y + dotpix)
                && matrix.get(x + dotpix, y + 2 * dotpix)
                && b.containsKey(x + ":" + y)
                && b.containsKey(x + ":" + (y + dotpix))
                && b.containsKey(x + ":" + (y + 2 * dotpix))
                && b.containsKey((x + dotpix) + ":" + y)
                && b.containsKey((x + dotpix) + ":" + (y + dotpix))
                && b.containsKey((x + dotpix) + ":" + (y + 2 * dotpix))) {
              int k = random.nextInt((elements.get(J2X3).size()));
              g.drawImage(ImageIO.read(new File(elements.get(J2X3).get(k))), x, y, null, null);
              b.remove(x + ":" + y);
              b.remove(x + ":" + (y + dotpix));
              b.remove(x + ":" + (y + 2 * dotpix));
              b.remove((x + dotpix) + ":" + y);
              b.remove((x + dotpix) + ":" + (y + dotpix));
              b.remove((x + dotpix) + ":" + (y + 2 * dotpix));
            }
          }
        }
      }
    }
    // 1x3
    if (elements.containsKey(J1X3) && !elements.get(J1X3).isEmpty()) {
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          if (x % dotpix == 0 && y % dotpix == 0) {
            if (((y + 2 * dotpix) < height)
                && matrix.get(x, y)
                && matrix.get(x, y + dotpix)
                && matrix.get(x, y + 2 * dotpix)
                && b.containsKey(x + ":" + y)
                && b.containsKey(x + ":" + (y + dotpix))
                && b.containsKey(x + ":" + (y + 2 * dotpix))) {

              int k = random.nextInt((elements.get(J1X3).size()));
              g.drawImage(ImageIO.read(new File(elements.get(J1X3).get(k))), x, y, null, null);
              b.remove(x + ":" + y);
              b.remove(x + ":" + (y + dotpix));
              b.remove(x + ":" + (y + 2 * dotpix));
            }
          }
        }
      }
    }

    // 2x2
    if (elements.containsKey(J2X2) && !elements.get(J2X2).isEmpty()) {
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          if (x % dotpix == 0 && y % dotpix == 0) {
            if (((x + dotpix) < width)
                && ((y + dotpix) < height)
                && matrix.get(x, y)
                && matrix.get(x, y + dotpix)
                && matrix.get(x + dotpix, y)
                && matrix.get(x + dotpix, y + dotpix)
                && b.containsKey(x + ":" + y)
                && b.containsKey(x + ":" + (y + dotpix))
                && b.containsKey((x + dotpix) + ":" + y)
                && b.containsKey((x + dotpix) + ":" + (y + dotpix))) {
              int k = random.nextInt((elements.get(J2X2).size()));
              g.drawImage(ImageIO.read(new File(elements.get(J2X2).get(k))), x, y, null, null);
              b.remove(x + ":" + y);
              b.remove(x + ":" + (y + dotpix));
              b.remove((x + dotpix) + ":" + y);
              b.remove((x + dotpix) + ":" + (y + dotpix));
            }
          }
        }
      }
    }
    // 2x1
    if (elements.containsKey(J2X1) && !elements.get(J2X1).isEmpty()) {
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          if (x % dotpix == 0 && y % dotpix == 0) {
            if (((x + dotpix) < width)
                && matrix.get(x, y)
                && matrix.get(x + dotpix, y)
                && b.containsKey(x + ":" + y)
                && b.containsKey((x + dotpix) + ":" + y)) {
              int k = random.nextInt((elements.get(J2X1).size()));
              g.drawImage(ImageIO.read(new File(elements.get(J2X1).get(k))), x, y, null, null);
              b.remove(x + ":" + y);
              b.remove((x + dotpix) + ":" + y);
            }
          }
        }
      }
    }

    // 1x2
    if (elements.containsKey(J1X2) && !elements.get(J1X2).isEmpty()) {
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          if (x % dotpix == 0 && y % dotpix == 0) {
            if (((y + dotpix) < height)
                && matrix.get(x, y)
                && matrix.get(x, y + dotpix)
                && b.containsKey(x + ":" + y)
                && b.containsKey(x + ":" + (y + dotpix))) {

              int k = random.nextInt((elements.get(J1X2).size()));
              g.drawImage(ImageIO.read(new File(elements.get(J1X2).get(k))), x, y, null, null);
              b.remove(x + ":" + y);
              b.remove(x + ":" + (y + dotpix));
            }
          }
        }
      }
    }
    // 1x1
    if (elements.containsKey(J1X1) && !elements.get(J1X1).isEmpty()) {
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          if (x % dotpix == 0 && y % dotpix == 0) {
            if (matrix.get(x, y) && b.containsKey(x + ":" + y)) {
              int k = random.nextInt((elements.get(J1X1).size()));
              g.drawImage(ImageIO.read(new File(elements.get(J1X1).get(k))), x, y, null, null);
              b.remove(x + ":" + y);
            }
          }
        }
      }
    }
  }
}
