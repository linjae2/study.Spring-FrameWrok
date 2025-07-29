package org.snudh.api;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import io.micrometer.common.util.StringUtils;

import javax.imageio.ImageIO;


@Controller
@RequestMapping("/api/v1/barcode")
public class BarcodeController {

  @RequestMapping("/{barcode}")
  public ResponseEntity<byte[]> getBarcodeImage(@PathVariable String barcode) {
    if (StringUtils.isEmpty(barcode)) return null;

    try {
      //Graphics2D 
      BufferedImage crunchifyImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
      Graphics2D g2d = (Graphics2D) crunchifyImage.getGraphics();
      g2d.setColor(Color.white); // 배경 색 설정
      g2d.fillRect(0, 0, 150, 150);

      g2d.setColor(Color.BLACK); // 코드 색 설정
      g2d.fillRect(100, 100, 200, 200);

      // Set font and color
      g2d.setFont(new Font("Arialasdad", Font.BOLD, 24));
      g2d.setColor(Color.BLACK);
      g2d.drawString("This is gona be awesome",20,20);

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      //PngEncoder encoder = new PngEncoder();
      ImageIO.write(crunchifyImage, "png", baos);
      //JDeli.write(bufferedImage, "heic", outputStreamOrFile);

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.IMAGE_PNG);

			return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @RequestMapping("/test_02/{barcode}")
  public ResponseEntity<byte[]> getTest02BarcodeImage(@PathVariable String barcode) {
    if (StringUtils.isEmpty(barcode)) return null;

    try {
      Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
      hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
      Writer writer = new Code128Writer();
			BitMatrix bitMatrix = writer.encode(barcode, BarcodeFormat.CODE_128, 300, 125);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			MatrixToImageWriter.writeToStream(bitMatrix, "png", byteArrayOutputStream);

      HttpHeaders headers = new HttpHeaders();
      MediaType mType = MediaType.IMAGE_PNG;

      headers.setContentType(mType);

			return new ResponseEntity<>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @RequestMapping("/test_01/{barcode}")
  public @ResponseBody byte[] getTest01BarcodeImage(@PathVariable String barcode) {
    if (StringUtils.isEmpty(barcode)) return null;

    try {
      Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
      hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
      Writer writer = new Code128Writer();
			BitMatrix bitMatrix = writer.encode(barcode, BarcodeFormat.CODE_128, 300, 125);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			MatrixToImageWriter.writeToStream(bitMatrix, "png", byteArrayOutputStream);
			return byteArrayOutputStream.toByteArray();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
