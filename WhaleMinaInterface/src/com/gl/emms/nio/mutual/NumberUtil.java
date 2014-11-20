package com.gl.emms.nio.mutual;

/**
 * @author yang3wei
 * int��byte[] �໥ת���Ĺ�����~
 */
public class NumberUtil {

  /**
   * ������ת��Ϊ�ֽ�����~
   * @param integer
   * @return
   */
  public static byte[] int2bytes(int integer) {
    byte[] bytes = new byte[4];
    bytes[0] = (byte) (integer & 0xff); // ���λ
    bytes[1] = (byte) ((integer >> 8) & 0xff); // �ε�λ
    bytes[2] = (byte) ((integer >> 16) & 0xff); // �θ�λ
    bytes[3] = (byte) (integer >>> 24); // ���λ���޷������ơ�
    return bytes;
  }

  /**
   * ���ֽ�����ת��Ϊ����~
   * @param bytes
   * @return
   */
  public static int bytes2int(byte[] bytes) {
    // һ�� byte �������� 24 λ��� 0x??000000�������� 8 λ��� 0x00??0000��| ��ʾ��λ��
    int integer = (bytes[0] & 0xff) 
        | ((bytes[1] << 8) & 0xff00) 
        | ((bytes[2] << 24) >>> 8) 
        | (bytes[3] << 24);
    return integer;
  }
}
