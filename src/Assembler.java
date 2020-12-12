
/**
* Assembler for the CS318 simple computer simulation
*
* @author Mariana De Luna
*/
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Assembler {

	/**
	 * Definitions of opcodes. LSB is at array index 0, so the opcodes are written
	 * right to left (reverse of reading in English)
	 */
	private static final boolean[] OP_ADD = { false, false, false, true, true, false, true, false, false, false, true };
	private static final boolean[] OP_SUB = { false, false, false, true, true, false, true, false, false, true, true };
	private static final boolean[] OP_AND = { false, false, false, false, true, false, true, false, false, false, true };
	private static final boolean[] OP_ORR = { false, false, false, false, true, false, true, false, true, false, true };
	private static final boolean[] OP_LDR = { false, true, false, false, false, false, true, true, true, true, true };
	private static final boolean[] OP_STR = { false, false, false, false, false, false, true, true, true, true, true };
	private static final boolean[] OP_CBZ = { false, false, true, false, true, true, false, true };
	private static final boolean[] OP_B = { true, false, true, false, false, false };
	private static final boolean[] OP_HLT = { false, true, false, false, false, true, false, true, false, true, true };

	/**
	 * Assembles the code file. When this method is finished, the dataFile and
	 * codeFile contain the assembled data segment and code segment, respectively.
	 *
	 * @param inFile
	 *            The pathname to the assembly language file to be assembled.
	 * @param dataFile
	 *            The pathname where the data segment file should be written.
	 * @param codeFile
	 *            The pathname where the code segment file should be written.
	 */
	public static void assemble(String inFile, String dataFile, String codeFile)
			throws FileNotFoundException, IOException {

		// do not make any changes to this method

		ArrayList<LabelOffset> labels = pass1(inFile, dataFile, codeFile);
		pass2(inFile, dataFile, codeFile, labels);
	}

	/**
	 * First pass of the assembler. Writes the number of bytes in the data segment
	 * and code segment to their respective output files. Returns a list of code
	 * segment labels and their relative offsets.
	 *
	 * @param inFile
	 *            The pathname of the file containing assembly language code.
	 * @param dataFile
	 *            The pathname for the data segment binary file.
	 * @param codeFile
	 *            The pathname for the code segment binary file.
	 * @return List of the code segment labels and relative offsets.
	 * @exception RuntimeException
	 *                if the assembly code file does not have the correct format, or
	 *                another error while processing the assembly code file.
	 */

	private static ArrayList<LabelOffset> pass1(String inFile, String dataFile, String codeFile)
			throws FileNotFoundException {

		// Student must complete this method

		File file = new File(inFile);
		Scanner sc = new Scanner(file);
		String temp = sc.nextLine();
		ArrayList<LabelOffset> lo = new ArrayList<LabelOffset>();

		// calculates the number of bytes in the data segment

		while (!temp.contains(".data")) {
			temp = sc.nextLine();
		} // end while

		while (!temp.contains(".word")) {
			temp = sc.nextLine();
		} // end while

		String[] arrayNums;
		int dataSize = 0;
		while (temp.contains(".word")) {
			temp = temp.replaceAll(".word", "");
			arrayNums = temp.split(",");
			for (int i = 0; i < arrayNums.length; i++) {
				dataSize += 4;
			} // end for
			temp = sc.nextLine();
		} // end while

		// write byte size to file
		try {
			FileWriter fileWriter = new FileWriter(dataFile);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(dataSize + "");
			bufferedWriter.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} // end try-catch

		// calculate bytes for code segment and create array of labels
		int codeSize = 0;
		while (!temp.contains(".global main")) {
			temp = sc.nextLine();
		} // end while

		while (!temp.contains(".end")) {
			String[] inst;
			inst = temp.split(" ");
			for (int i = 0; i < inst.length; i++) {
				if (inst[i].contains("ADD")) {
					codeSize += 4;
				} // end if
				if (inst[i].equals("SUB")) {
					codeSize += 4;
				} // end if
				if (inst[i].contains("AND")) {
					codeSize += 4;
				} // end if
				if (inst[i].contains("ORR")) {
					codeSize += 4;
				} // end if
				if (inst[i].contains("LDR")) {
					codeSize += 4;
				} // end if
				if (inst[i].contains("STR")) {
					codeSize += 4;
				} // end if
				if (inst[i].equals("CBZ")) {
					codeSize += 4;
				} // end if
				if (inst[i].equals("B")) {
					codeSize += 4;
				} // end if
				if (inst[i].contains(":")) {
					// create labels
					temp = temp.replaceAll(":", "");
					LabelOffset label = new LabelOffset();
					label.label = temp;
					label.offset = codeSize + 4;
					lo.add(label);
				} // end if
			} // end for
			temp = sc.nextLine();
		} // end while

		// represents the bytes for HLT instruction
		if (temp.contains(".end")) {
			codeSize += 4;
		} // end if

		// write byte size to file
		try {
			FileWriter fileWriter = new FileWriter(codeFile);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(codeSize + "");
			bufferedWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // end try-catch

		// placeholder return. Student should replace with correct return.
		return lo;
	} // end of pass1

	/**
	 * Second pass of the assembler. Writes the binary data and code files.
	 * 
	 * @param inFile
	 *            The pathname of the file containing assembly language code.
	 * @param dataFile
	 *            The pathname for the data segment binary file.
	 * @param codeFile
	 *            The pathname for the code segment binary file.
	 * @param labels
	 *            List of the code segment labels and relative offsets.
	 * @exception RuntimeException
	 *                if there is an error when processing the assembly code file.
	 */

	public static void pass2(String inFile, String dataFile, String codeFile, ArrayList<LabelOffset> labels)
			throws FileNotFoundException, IOException {

		// Student must complete this method
		File file = new File(inFile);
		Scanner sc = new Scanner(file);
		String temp = sc.nextLine();
		String[] arrayNums;
		boolean[] binaryNumList;

		while (!temp.contains(".data")) {
			temp = sc.nextLine();
		} // end while

		while (!temp.contains(".word")) {
			temp = sc.nextLine();
		} // end while

		// write binary data segment to data file
		while (temp.contains(".word")) {
			temp = temp.replaceAll(".word", "");
			arrayNums = temp.split(",");
			for (int i = 0; i < arrayNums.length; i++) {
				binaryNumList = Binary.sDecToBin(Long.parseLong(arrayNums[i].trim()));
				FileWriter fileWriter = new FileWriter(dataFile, true);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				for (int j = 0; j < binaryNumList.length; j++) {
					if (j % 8 == 0) {
						bufferedWriter.newLine();
					} // end if
					bufferedWriter.write(Boolean.toString(binaryNumList[j]) + " ");
				} // end for
				bufferedWriter.close();
			} // end for
			temp = sc.nextLine();
		} // end while

		while (!temp.contains(".global main")) {
			temp = sc.nextLine();
		} // end while

		int instOff = 0; // keep track of instruction offset
		// write binary machine language code segment to file
		while (!temp.contains(".end")) {
			int inst = 0;
			if (temp.contains("ADD") || temp.contains("SUB") || temp.contains("AND") || temp.contains("ORR")) {
				instOff += 4;
				try {
					FileWriter fileWriter = new FileWriter(codeFile, true);
					BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
					if (temp.contains("ADD")) {
						temp = temp.replaceAll("ADD ", "");
						inst = 0;
					} else if (temp.contains("SUB")) {
						temp = temp.replaceAll("SUB ", "");
						inst = 1;
					} else if (temp.contains("AND")) {
						temp = temp.replaceAll("AND ", "");
						inst = 2;
					} else if (temp.contains("ORR")) {
						temp = temp.replaceAll("ORR ", "");
						inst = 3;
					} // end if
					temp = temp.trim();
					String[] registers = temp.split(",");
					bufferedWriter.newLine();
					registers[0] = registers[0].replaceAll("R", "");
					boolean[] destReg = Binary.sDecToBin(Long.parseLong(registers[0]));
					for (int i = 0; i < 5; i++) {
						bufferedWriter.write(Boolean.toString(destReg[i]) + " ");
					} // end for

					registers[1] = registers[1].replaceAll("R", "");
					boolean[] register1 = Binary.sDecToBin(Long.parseLong(registers[1]));
					for (int i = 0; i < 5; i++) {
						if (i == 3) {
							bufferedWriter.newLine();
						} // end if
						bufferedWriter.write(Boolean.toString(register1[i]) + " ");
					} // end for

					boolean[] shift = Binary.sDecToBin(0);
					for (int i = 0; i < 6; i++) {
						bufferedWriter.write(Boolean.toString(shift[i]) + " ");
					} // end for

					bufferedWriter.newLine();
					registers[2] = registers[2].replaceAll("R", "");
					boolean[] register2 = Binary.sDecToBin(Long.parseLong(registers[2]));
					for (int i = 0; i < 5; i++) {
						bufferedWriter.write(Boolean.toString(register2[i]) + " ");
					} // end for

					// ADD bits
					if (inst == 0) {
						for (int i = 0; i < OP_ADD.length; i++) {
							bufferedWriter.write(Boolean.toString(OP_ADD[i]) + " ");
							if (i == 2) {
								bufferedWriter.newLine();
							} // end if
						} // end for
					} else if (inst == 1) {
						for (int i = 0; i < OP_SUB.length; i++) {
							bufferedWriter.write(Boolean.toString(OP_SUB[i]) + " ");
							if (i == 2) {
								bufferedWriter.newLine();
							} // end if
						} // end for
					} else if (inst == 2) {
						for (int i = 0; i < OP_AND.length; i++) {
							bufferedWriter.write(Boolean.toString(OP_AND[i]) + " ");
							if (i == 2) {
								bufferedWriter.newLine();
							} // end if
						} // end for
					} else if (inst == 3) {
						for (int i = 0; i < OP_ORR.length; i++) {
							bufferedWriter.write(Boolean.toString(OP_ORR[i]) + " ");
							if (i == 2) {
								bufferedWriter.newLine();
							} // end if
						} // end for
					} // end if
					bufferedWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // end try-catch
			} // end if

			if (temp.contains("LDR") || temp.contains("STR")) {
				instOff += 4;
				inst = 0;
				try {
					FileWriter fileWriter = new FileWriter(codeFile, true);
					BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
					if (temp.contains("LDR")) {
						temp = temp.replaceAll("LDR ", "");
						inst = 0;
					} else if (temp.contains("STR")) {
						temp = temp.replaceAll("STR ", "");
						inst = 1;
					} // end if
					temp = temp.trim();
					String[] registers = temp.split(",");
					bufferedWriter.newLine();
					registers[0] = registers[0].replaceAll("R", "");
					boolean[] valReg = Binary.sDecToBin(Long.parseLong(registers[0]));
					for (int i = 0; i < 5; i++) {
						bufferedWriter.write(Boolean.toString(valReg[i]) + " ");
					} // end for

					registers[1] = registers[1].replaceAll("\\[R", "");
					boolean[] baseReg = Binary.sDecToBin(Long.parseLong(registers[1]));
					for (int i = 0; i < 5; i++) {
						if (i == 3) {
							bufferedWriter.newLine();
						} // end if
						bufferedWriter.write(Boolean.toString(baseReg[i]) + " ");
					} // end for

					boolean[] shift = Binary.sDecToBin(0);
					for (int i = 0; i < 2; i++) {
						bufferedWriter.write(Boolean.toString(shift[i]) + " ");
					} // end for

					registers[2] = registers[2].replaceAll("#", "");
					registers[2] = registers[2].replaceAll("]", "");
					boolean[] immediate = Binary.sDecToBin(Long.parseLong(registers[2]));
					for (int i = 0; i < 9; i++) {
						if (i == 4) {
							bufferedWriter.newLine();
						} // end if
						bufferedWriter.write(Boolean.toString(immediate[i]) + " ");
					} // end for

					if (inst == 0) {
						for (int i = 0; i < OP_LDR.length; i++) {
							bufferedWriter.write(Boolean.toString(OP_LDR[i]) + " ");
							if (i == 2) {
								bufferedWriter.newLine();
							} // end if
						} // end for
					} else if (inst == 1) {
						for (int i = 0; i < OP_STR.length; i++) {
							bufferedWriter.write(Boolean.toString(OP_STR[i]) + " ");
							if (i == 2) {
								bufferedWriter.newLine();
							} // end if
						} // end for
					} // end if
					bufferedWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // end try-catch
			} // end if

			if (temp.contains("CBZ")) {
				instOff += 4;
				try {
					FileWriter fileWriter = new FileWriter(codeFile, true);
					BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
					temp = temp.replaceAll("CBZ ", "");
					temp = temp.trim();
					String[] registers = temp.split(",");
					registers[0] = registers[0].replaceAll("R", "");
					boolean[] reg = Binary.sDecToBin(Long.parseLong(registers[0]));
					for (int i = 0; i < 5; i++) {
						if (i == 0) {
							bufferedWriter.newLine();
						} // end if
						bufferedWriter.write(Boolean.toString(reg[i]) + " ");
					} // end for

					for (int k = 0; k < labels.size(); k++) {
						String branchLabel = labels.get(k).label.toString(); // returns a string object
						if (branchLabel.equals(registers[1])) {
							int offset = labels.get(k).offset - instOff; // label offset - current offset
							boolean[] immediate = Binary.sDecToBin((offset));
							for (int i = 0; i < 19; i++) {
								if (i == 3 || i == 11) {
									bufferedWriter.newLine();
								} // end if
								bufferedWriter.write(Boolean.toString(immediate[i]) + " ");
							} // end for
						} // end if
					} // end for

					bufferedWriter.newLine();
					for (int i = 0; i < OP_CBZ.length; i++) {
						bufferedWriter.write(Boolean.toString(OP_CBZ[i]) + " ");
					} // end for
					bufferedWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // end try-catch
			} // end if

			if (temp.contains("B")) {
				temp = temp.trim();
				instOff += 4;
				String[] elements = temp.split(" ");
				if (elements[0].equals("B")) {
					try {
						FileWriter fileWriter = new FileWriter(codeFile, true);
						BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
						temp = temp.replaceAll("B ", "");
						String label = elements[1];
						for (int k = 0; k < labels.size(); k++) {
							String branchLabel = labels.get(k).label.toString(); // returns a string object
							if (branchLabel.equals(label)) {
								int offset = labels.get(k).offset - instOff; // label offset - current offset
								boolean[] immediate = Binary.sDecToBin((offset));
								for (int i = 0; i < 26; i++) {
									if (i % 8 == 0) {
										bufferedWriter.newLine();
									} // end if
									bufferedWriter.write(Boolean.toString(immediate[i]) + " ");
								} // end for
							} // end if
						} // end for
						for (int i = 0; i < OP_B.length; i++) {
							bufferedWriter.write(Boolean.toString(OP_B[i]) + " ");
						} // end for
						bufferedWriter.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // end try-catch
				} else {
					// do nothing
				} // end if
			} // end if

			temp = sc.nextLine();
		} // end while

		// represents HLT instruction
		if (temp.contains(".end")) {
			int j = 0;
			try {
				FileWriter fileWriter = new FileWriter(codeFile, true);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				for (j = 0; j < 21; j++) {
					if (j % 8 == 0) {
						bufferedWriter.newLine();
					} // end if
					if (j > -1 && j < 21) {
						bufferedWriter.write(false + " ");
					} // end if
					if (j >= 20) {
						for (int i = 0; i < OP_HLT.length; i++) {
							bufferedWriter.write(Boolean.toString(OP_HLT[i]) + " ");
							if (i == 2) {
								bufferedWriter.newLine();
							} // end if
						} // end for
					} // end if
				} // end for
				bufferedWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // end try-catch
		} // end if

	} // end of pass2

} // end Class Assembler
