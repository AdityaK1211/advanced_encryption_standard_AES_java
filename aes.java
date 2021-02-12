import java.util.*;
import java.io.*;

class test
{
	public static String keystr;
	public static int nr, nb, nk;
	public static String key = "";
    public static String iv = "";
    public static String ftw = "";
    public static int keyFileIndex = 1;
	public static final String newline = System.getProperty("line.separator"); 
	
	public static final int[][] galois = {{0x02, 0x03, 0x01, 0x01},
        {0x01, 0x02, 0x03, 0x01},
        {0x01, 0x01, 0x02, 0x03},
        {0x03, 0x01, 0x01, 0x02}};
		
	public static final int[][] invgalois = {{0x0e, 0x0b, 0x0d, 0x09},
        {0x09, 0x0e, 0x0b, 0x0d},
        {0x0d, 0x09, 0x0e, 0x0b},
        {0x0b, 0x0d, 0x09, 0x0e}};
	
	public static final int[][] sbox = {{0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76}, 
		{0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0}, 
		{0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15}, 
		{0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75}, 
		{0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84}, 
		{0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf}, 
		{0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8}, 
		{0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2}, 
		{0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73}, 
		{0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb}, 
		{0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79}, 
		{0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08}, 
		{0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a}, 
		{0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e}, 
		{0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf}, 
		{0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16}};
    /**
     * Inverse SBOX table used for invSubBytes
     */
    public static final int[][] invsbox = {{0x52, 0x09, 0x6a, 0xd5, 0x30, 0x36, 0xa5, 0x38, 0xbf, 0x40, 0xa3, 0x9e, 0x81, 0xf3, 0xd7, 0xfb}, 
		{0x7c, 0xe3, 0x39, 0x82, 0x9b, 0x2f, 0xff, 0x87, 0x34, 0x8e, 0x43, 0x44, 0xc4, 0xde, 0xe9, 0xcb}, 
		{0x54, 0x7b, 0x94, 0x32, 0xa6, 0xc2, 0x23, 0x3d, 0xee, 0x4c, 0x95, 0x0b, 0x42, 0xfa, 0xc3, 0x4e}, 
		{0x08, 0x2e, 0xa1, 0x66, 0x28, 0xd9, 0x24, 0xb2, 0x76, 0x5b, 0xa2, 0x49, 0x6d, 0x8b, 0xd1, 0x25}, 
		{0x72, 0xf8, 0xf6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xd4, 0xa4, 0x5c, 0xcc, 0x5d, 0x65, 0xb6, 0x92}, 
		{0x6c, 0x70, 0x48, 0x50, 0xfd, 0xed, 0xb9, 0xda, 0x5e, 0x15, 0x46, 0x57, 0xa7, 0x8d, 0x9d, 0x84}, 
		{0x90, 0xd8, 0xab, 0x00, 0x8c, 0xbc, 0xd3, 0x0a, 0xf7, 0xe4, 0x58, 0x05, 0xb8, 0xb3, 0x45, 0x06}, 
		{0xd0, 0x2c, 0x1e, 0x8f, 0xca, 0x3f, 0x0f, 0x02, 0xc1, 0xaf, 0xbd, 0x03, 0x01, 0x13, 0x8a, 0x6b}, 
		{0x3a, 0x91, 0x11, 0x41, 0x4f, 0x67, 0xdc, 0xea, 0x97, 0xf2, 0xcf, 0xce, 0xf0, 0xb4, 0xe6, 0x73}, 
		{0x96, 0xac, 0x74, 0x22, 0xe7, 0xad, 0x35, 0x85, 0xe2, 0xf9, 0x37, 0xe8, 0x1c, 0x75, 0xdf, 0x6e}, 
		{0x47, 0xf1, 0x1a, 0x71, 0x1d, 0x29, 0xc5, 0x89, 0x6f, 0xb7, 0x62, 0x0e, 0xaa, 0x18, 0xbe, 0x1b}, 
		{0xfc, 0x56, 0x3e, 0x4b, 0xc6, 0xd2, 0x79, 0x20, 0x9a, 0xdb, 0xc0, 0xfe, 0x78, 0xcd, 0x5a, 0xf4}, 
		{0x1f, 0xdd, 0xa8, 0x33, 0x88, 0x07, 0xc7, 0x31, 0xb1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xec, 0x5f}, 
		{0x60, 0x51, 0x7f, 0xa9, 0x19, 0xb5, 0x4a, 0x0d, 0x2d, 0xe5, 0x7a, 0x9f, 0x93, 0xc9, 0x9c, 0xef}, 
		{0xa0, 0xe0, 0x3b, 0x4d, 0xae, 0x2a, 0xf5, 0xb0, 0xc8, 0xeb, 0xbb, 0x3c, 0x83, 0x53, 0x99, 0x61}, 
		{0x17, 0x2b, 0x04, 0x7e, 0xba, 0x77, 0xd6, 0x26, 0xe1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0c, 0x7d}};
	
	// rcon array
	public static final int[] rcon = {0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a,
        0x2f, 0x5e, 0xbc, 0x63, 0xc6, 0x97, 0x35, 0x6a, 0xd4, 0xb3, 0x7d, 0xfa, 0xef, 0xc5, 0x91, 0x39,
        0x72, 0xe4, 0xd3, 0xbd, 0x61, 0xc2, 0x9f, 0x25, 0x4a, 0x94, 0x33, 0x66, 0xcc, 0x83, 0x1d, 0x3a,
        0x74, 0xe8, 0xcb, 0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8,
        0xab, 0x4d, 0x9a, 0x2f, 0x5e, 0xbc, 0x63, 0xc6, 0x97, 0x35, 0x6a, 0xd4, 0xb3, 0x7d, 0xfa, 0xef,
        0xc5, 0x91, 0x39, 0x72, 0xe4, 0xd3, 0xbd, 0x61, 0xc2, 0x9f, 0x25, 0x4a, 0x94, 0x33, 0x66, 0xcc,
        0x83, 0x1d, 0x3a, 0x74, 0xe8, 0xcb, 0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b,
        0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a, 0x2f, 0x5e, 0xbc, 0x63, 0xc6, 0x97, 0x35, 0x6a, 0xd4, 0xb3,
        0x7d, 0xfa, 0xef, 0xc5, 0x91, 0x39, 0x72, 0xe4, 0xd3, 0xbd, 0x61, 0xc2, 0x9f, 0x25, 0x4a, 0x94,
        0x33, 0x66, 0xcc, 0x83, 0x1d, 0x3a, 0x74, 0xe8, 0xcb, 0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20,
        0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a, 0x2f, 0x5e, 0xbc, 0x63, 0xc6, 0x97, 0x35,
        0x6a, 0xd4, 0xb3, 0x7d, 0xfa, 0xef, 0xc5, 0x91, 0x39, 0x72, 0xe4, 0xd3, 0xbd, 0x61, 0xc2, 0x9f,
        0x25, 0x4a, 0x94, 0x33, 0x66, 0xcc, 0x83, 0x1d, 0x3a, 0x74, 0xe8, 0xcb, 0x8d, 0x01, 0x02, 0x04,
        0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a, 0x2f, 0x5e, 0xbc, 0x63,
        0xc6, 0x97, 0x35, 0x6a, 0xd4, 0xb3, 0x7d, 0xfa, 0xef, 0xc5, 0x91, 0x39, 0x72, 0xe4, 0xd3, 0xbd,
        0x61, 0xc2, 0x9f, 0x25, 0x4a, 0x94, 0x33, 0x66, 0xcc, 0x83, 0x1d, 0x3a, 0x74, 0xe8, 0xcb};
	
	public static void main(String arr[])
	{
		Scanner sc = new Scanner(System.in);
		int ch, i, j;
		String inc, outc;
		
		do
		{
			System.out.print("\nADVANCED ENCRYPTION STANDARD AES CIPHER");
			System.out.print("\n--------------------------------------------------");
			System.out.print("\n1.Key Generation 2.Encryption 3.Decryption 0.Exit");
			System.out.print("\nEnter your choice: ");
			ch = sc.nextInt();
			switch(ch)
			{
				case 1: sc.nextLine();
						System.out.print("\nAES KEY GENERATION");
						System.out.print("\n--------------------------------------------------");
						int ch1;
						do
						{
							System.out.println("\n1.128-bits 2.192-bits 3.256-bits 0.Exit");
							System.out.print("Select a size: ");
							ch1 = sc.nextInt();
							switch(ch1)
							{
								case 1: System.out.println("\nSize 128-bits");
										nk = 128 / 32;
										nr = nk + 6;
										nb = 4;
										break;
										
								case 2: System.out.println("\nSize 192-bits");
										nk = 192 / 32;
										nr = nk + 6;
										nb = 4;
										break;
										
								case 3: System.out.println("\nSize 256-bits");
										nk = 256 / 32;
										nr = nk + 6;
										nb = 4;
										break;
										
								default: System.out.println();
										 break;
							}
						}
						while(ch1>=1 && ch1<=3);
						
						int keysize = nk*nb*2;
						System.out.println("Number of Rounds Nr: " + nr);
						System.out.println("Number of Key Blocks Nk: " + nk);
						System.out.println("Key Block Size Nb: " + nb);
						System.out.print("Enter Key of length " + keysize*4 + "-bits: ");
						sc.nextLine();
						// Key1: 2475A2B33475568831E2120013AA5487
						// Key2: 0F1571C947D9E8590CB7ADD6AF7F6798
						key = sc.nextLine();
						int n = key.length();
						while(n!=keysize)
						{
							if(n==keysize)
								break;
							System.out.print("\nPlease Enter Key of length " + keysize*4 + " only: ");
							key = sc.nextLine();
							n = key.length();	
						}
						//aesSubKey(nr, nk, nb);
						break;

				case 2: sc.nextLine();
						System.out.print("\nAES CIPHER ENCRYPTION");
						System.out.print("\n--------------------------------------------------");
						System.out.print("\nEnter input file for Encryption: ");
						inc = sc.nextLine();
						System.out.print("Enter output file for Encryption: ");
						outc = sc.nextLine();
						aesEncryption(inc, outc);
						break;

				case 3: sc.nextLine();
						System.out.print("\nAES CIPHER DECRYPTION");
						System.out.print("\n--------------------------------------------------");
						System.out.print("\nEnter input file for Decryption: ");
						inc = sc.nextLine();
						System.out.print("Enter output file for Decryption: ");
						outc = sc.nextLine();
						aesDecryption(inc, outc);
						break;

				default: System.out.println("\nEXIT!");
						 break;
			}
		}
		while(ch>=1 && ch<=3);
	}
	
	public static void aesEncryption(String inc, String outc) 
	{ 
		try
		{
			String plainText = "";
			int i, j;
			BufferedReader input = new BufferedReader(new FileReader(inc));
			FileWriter output = new FileWriter(outc);
			int[][] state, initvector = new int[4][4];
			int[][] keymatrix = keySchedule(key);
			
			System.out.print("\nPlain Text: "); 
			plainText = input.readLine();
			while(plainText != null)
			{
				System.out.print(plainText);
				plainText = input.readLine();
			}
			System.out.println("\nAES Key of " + (key.length())*4 +"-bits: " + key);
			
			//System.out.print("\nCipher Text: ");
			input = new BufferedReader(new FileReader(inc));
			plainText = "";
			plainText = input.readLine();
			while(plainText != null)
			{
				if(plainText.matches("[0-9A-F]+")) //If line is valid (i.e. contains valid hex characters, encrpyt. Otherwise, skip line. 
                {
                    if(plainText.length() < 32) 
					{
                        plainText = String.format("%032x",Integer.parseInt(plainText, 16));
                    }
                    state = new int[4][4];
                    for(i=0; i<4; i++) //Parses line into a matrix
                    {
                        for(j=0; j<4; j++)
						{
                            state[j][i] = Integer.parseInt(plainText.substring((8 * i) + (2 * j), (8 * i) + (2 * j + 2)), 16);
                        }
                    }
					System.out.println("\nInitial Add Round Key\n");
                    addRoundKey(state, subKey(keymatrix, 0)); //Starts the addRoundKey with the first part of Key Expansion
                    for(i=1; i<nr; i++)
					{
						System.out.println("\nRound " + i + "\t");
                        subBytes(state); //implements the Sub-Bytes subroutine.
                        shiftRows(state); //implements Shift-Rows subroutine.
                        mixColumns(state);
                        addRoundKey(state, subKey(keymatrix, i));
                    }
                    subBytes(state); //implements the Sub-Bytes subroutine.
                    shiftRows(state); //implements Shift-Rows subroutine.
                    System.out.println("\nRound 10\t");
					addRoundKey(state, subKey(keymatrix, nr));
                    
					System.out.println("\n\nCipher Text: " + (MatrixToString(state) + newline));
                    output.write(MatrixToString(state) + newline);
                    plainText = input.readLine();
				}
				else
				{
					plainText = input.readLine();
				}
			}
			System.out.println("AES encryption generated successfully. Check output file!\n");
			input.close();
			output.close();
		}
		catch(Exception e)
		{
			
		}
	}	
	
	public static void aesDecryption(String inc, String outc) 
	{ 
		try
		{
			String plainText = "";
			int i, j;
			BufferedReader input = new BufferedReader(new FileReader(inc));
			FileWriter output = new FileWriter(outc);
			int[][] state = new int[4][4];
            int[][] initvector = new int[4][4];
            int[][] nextvector = new int[4][4];
            int[][] keymatrix = keySchedule(key);
			
			while(plainText != null)
			{
				System.out.print(plainText);
				plainText = input.readLine();
			}
			System.out.println("\nAES Key of " + (key.length())*4 +"-bits: " + key);
			
			input = new BufferedReader(new FileReader(inc));
			plainText = "";
			plainText = input.readLine();
			while(plainText != null)
			{
				state = new int[4][4];
                for(i=0; i<state.length; i++) //Parses line into a matrix
                {
                    for(j=0; j<state[0].length; j++)
					{
                        state[j][i] = Integer.parseInt(plainText.substring((8 * i) + (2 * j), (8 * i) + (2 * j + 2)), 16);
                    }
                }
				System.out.println("\nInitial Add Round Key\n");
                addRoundKey(state, subKey(keymatrix, nr));
                for(i=nr-1; i>0; i--)
				{
					System.out.println("\nRound " + (i+1) + "\t");
                    invShiftRows(state);
                    invSubBytes(state);
                    addRoundKey(state, subKey(keymatrix, i));
                    invMixColumns(state);
                }
                invShiftRows(state);
                invSubBytes(state);
				System.out.println("\nRound 1\t");
                addRoundKey(state, subKey(keymatrix, 0));
                
				System.out.println("\n\nDecipher Text: " + (MatrixToString(state) + newline));
				output.write(MatrixToString(state) + newline);
                plainText = input.readLine();
			}
			System.out.println("\nAES decryption generated successfully. Check output file!\n");
			input.close();
			output.close();
		}
		catch(Exception e)
		{
			
		}
	}
	
	public static int[][] subKey(int[][] km, int begin) {
        int[][] arr = new int[4][4];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                arr[i][j] = km[i][4 * begin + j];
            }
        }
        return arr;
    }
	
	public static void subBytes(int[][] arr) {
        for (int i = 0; i < arr.length; i++) //Sub-Byte subroutine
        {
            for (int j = 0; j < arr[0].length; j++) {
                int hex = arr[j][i];
                arr[j][i] = sbox[hex / 16][hex % 16];
            }
        }
    }
	
	public static void invSubBytes(int[][] arr) {
        for (int i = 0; i < arr.length; i++) //Inverse Sub-Byte subroutine
        {
            for (int j = 0; j < arr[0].length; j++) {
                int hex = arr[j][i];
                arr[j][i] = invsbox[hex / 16][hex % 16];
            }
        }
    }
	
	public static void shiftRows(int[][] arr) {
        for (int i = 1; i < arr.length; i++) {
            arr[i] = leftrotate(arr[i], i);
        }
    }
	
	public static int[] leftrotate(int[] arr, int times)
    {
        assert(arr.length == 4);
        if (times % 4 == 0) {
            return arr;
        }
        while (times > 0) {
            int temp = arr[0];
            for (int i = 0; i < arr.length - 1; i++) {
                arr[i] = arr[i + 1];
            }
            arr[arr.length - 1] = temp;
            --times;
        }
        return arr;
    }
	
	public static void invShiftRows(int[][] arr) {
        for (int i = 1; i < arr.length; i++) {
            arr[i] = rightrotate(arr[i], i);
        }
    }
	
	public static int[] rightrotate(int[] arr, int times) {
        if (arr.length == 0 || arr.length == 1 || times % 4 == 0) {
            return arr;
        }
        while (times > 0) {
            int temp = arr[arr.length - 1];
            for (int i = arr.length - 1; i > 0; i--) {
                arr[i] = arr[i - 1];
            }
            arr[0] = temp;
            --times;
        }
        return arr;
    }

	public static void mixColumns(int[][] arr) //method for mixColumns
    {
        int[][] tarr = new int[4][4];
        for(int i = 0; i < 4; i++)
        {
            System.arraycopy(arr[i], 0, tarr[i], 0, 4);
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                arr[i][j] = mcHelper(tarr, galois, i, j);
            }
        }
    }
	
	public static int mcHelper(int[][] arr, int[][] g, int i, int j)
    {
        int mcsum = 0;
        for (int k = 0; k < 4; k++) {
            int a = g[i][k];
            int b = arr[k][j];
            mcsum ^= mcCalc(a, b);
        }
        return mcsum;
    }

    public static int mcCalc(int a, int b) //Helper method for mcHelper
    {
        if (a == 1) {
            return b;
        } else if (a == 2) {
            return MCTables.mc2[b / 16][b % 16];
        } else if (a == 3) {
            return MCTables.mc3[b / 16][b % 16];
        }
        return 0;
    }

    public static void invMixColumns(int[][] arr) {
        int[][] tarr = new int[4][4];
        for(int i = 0; i < 4; i++)
        {
            System.arraycopy(arr[i], 0, tarr[i], 0, 4);
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                arr[i][j] = invMcHelper(tarr, invgalois, i, j);
            }
        }
    }

    public static int invMcHelper(int[][] arr, int[][] igalois, int i, int j) //Helper method for invMixColumns
    {
        int mcsum = 0;
        for (int k = 0; k < 4; k++) {
            int a = igalois[i][k];
            int b = arr[k][j];
            mcsum ^= invMcCalc(a, b);
        }
        return mcsum;
    }
	
	public static int[][] keySchedule(String key)
    {

        int binkeysize = key.length() * 4;
        int colsize = binkeysize + 48 - (32 * ((binkeysize / 64) - 2)); //size of key scheduling will be based on the binary size of the key.
        int[][] keyMatrix = new int[4][colsize / 4]; //creates the matrix for key scheduling
        int rconpointer = 1;
        int[] t = new int[4];
        final int keycounter = binkeysize / 32;
        int k;

        for (int i = 0; i < keycounter; i++) //the first 1 (128-bit key) or 2 (256-bit key) set(s) of 4x4 matrices are filled with the key.
        {
            for (int j = 0; j < 4; j++) {
                keyMatrix[j][i] = Integer.parseInt(key.substring((8 * i) + (2 * j), (8 * i) + (2 * j + 2)), 16);
            }
        }
        int keypoint = keycounter;
        while (keypoint < (colsize / 4)) {
            int temp = keypoint % keycounter;
            if (temp == 0) {
                for (k = 0; k < 4; k++) {
                    t[k] = keyMatrix[k][keypoint - 1];
                }
                t = schedule_core(t, rconpointer++);
                for (k = 0; k < 4; k++) {
                    keyMatrix[k][keypoint] = t[k] ^ keyMatrix[k][keypoint - keycounter];
                }
                keypoint++;
            } else if (temp == 4) {
                for (k = 0; k < 4; k++) {
                    int hex = keyMatrix[k][keypoint - 1];
                    keyMatrix[k][keypoint] = sbox[hex / 16][hex % 16] ^ keyMatrix[k][keypoint - keycounter];
                }
                keypoint++;
            } else {
                int ktemp = keypoint + 3;
                while (keypoint < ktemp) {
                    for (k = 0; k < 4; k++) {
                        keyMatrix[k][keypoint] = keyMatrix[k][keypoint - 1] ^ keyMatrix[k][keypoint - keycounter];
                    }
                    keypoint++;
                }
            }
        }
        return keyMatrix;
    }
	
	public static int[] schedule_core(int[] in, int rconpointer) {
        in = leftrotate(in, 1);
        int hex;
        for (int i = 0; i < in.length; i++) {
            hex = in[i];
            in[i] = sbox[hex / 16][hex % 16];
        }
        in[0] ^= rcon[rconpointer];
        return in;
    }
	
	public static int invMcCalc(int a, int b) //Helper method for invMcHelper
    {
        if (a == 9) {
            return MCTables.mc9[b / 16][b % 16];
        } else if (a == 0xb) {
            return MCTables.mc11[b / 16][b % 16];
        } else if (a == 0xd) {
            return MCTables.mc13[b / 16][b % 16];
        } else if (a == 0xe) {
            return MCTables.mc14[b / 16][b % 16];
        }
        return 0;
    }
	
	public static void addRoundKey(int[][] bytematrix, int[][] keymatrix)
    {
        for(int i = 0; i < bytematrix.length; i++) 
		{
            for(int j = 0; j < bytematrix[0].length; j++) 
			{
                bytematrix[j][i] ^= keymatrix[j][i];
				System.out.print(bytematrix[j][i]);
            }
        }
    }
	
	public static String MatrixToString(int[][] m) //takes in a matrix and converts it into a line of 32 hex characters.
    {
        String t = "";
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                String h = Integer.toHexString(m[j][i]).toUpperCase();
                if (h.length() == 1) {
                    t += '0' + h;
                } else {
                    t += h;
                }
            }
        }
        return t;
    }
}
