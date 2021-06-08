import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.sun.javadoc.Parameter;

public class ExamScoreCalculator {

	static TreeMap<String, String> TRANSITIONS;
	static HashMap<String, HashMap<String, Double>> TRANSITIONSParameterValues;
	static TreeMap<String, HashMap<String, Double>> MUTATION_ALGORITHMS;
//	static String faultyTransition = "";
	static ArrayList<String> faultyTransitions = new ArrayList<String>();

	public static void main(String[] args) {

		// Assumptions
		// TODO: you need to find the best way to save the mutants (input)

		TRANSITIONS = new TreeMap<String, String>();
		TRANSITIONSParameterValues = new HashMap<String, HashMap<String, Double>>();
		MUTATION_ALGORITHMS = new TreeMap<String, HashMap<String, Double>>();

		// =========================================Mutant1
		// TRANSITIONS.put("t2", "Normal -> Normal { [(self.elements->size <=
		// (self.capacity - 1))] put(p : Element) }"); // t2
		// =========================================Mutant2
		// TRANSITIONS.put("t2", "Normal -> Normal { [(self.elements->size =
		// (self.capacity - 1))] put(p : Element) }"); // t2

		// Get all mutant files
		File folder = new File("mutants");
		File[] listOfFiles = folder.listFiles();

		// Create SBFL_TABLE
		TreeMap<String, HashMap<String, Boolean>> SBFL_TABLE = new TreeMap<String, HashMap<String, Boolean>>();
		HashMap<String, HashMap<String, HashMap<String, Double>>> PARAMETER_TABLE = new HashMap<String, HashMap<String, HashMap<String, Double>>>();
		HashMap<String, Double> EXAMSCORE_TABLE = new HashMap<String, Double>();

		for (File file : listOfFiles) {
			if (SBFL_TABLE.get(file.getName()) != null || file.getName().indexOf("transitions") > -1)
				continue;

			// Remove previous mutants to default state
			String mutantName = file.getName().replaceFirst("[.][^.]+$", "");
			ResetTransitionToDefaultState(mutantName);

//			String MutantName = file.getName().replaceFirst("[.][^.]+$", "");
//			switch (MutantName) {
//			case "outputMutant01":
//				faultyTransition = "t2";
//				TRANSITIONS.put("t2",
//						"Normal -> Normal { [(self.elements->size <= (self.capacity - 1))] put(p : Element) }"); // t2
//				break;
//			case "outputMutant02":
//				faultyTransition = "t2";
//				TRANSITIONS.put("t2",
//						"Normal -> Normal { [(self.elements->size = (self.capacity - 1))] put(p : Element) }"); // t2
//				break;
//			case "outputMutant03":
//				faultyTransition = "t2";
//				TRANSITIONS.put("t2",
//						"Normal -> Normal { [(self.elements->size = (self.capacity + 1))] put(p : Element) }"); // t2
//				break;
//			case "outputMutant04":
//				faultyTransition = "t2";
//				TRANSITIONS.put("t2",
//						"Normal -> Normal { [(self.elements->size = (self.capacity - 1))] get(p : Element) }"); // t2
//				break;
//			case "outputMutant06":
//				faultyTransition = "t8";
//				TRANSITIONS.put("t8", "Normal -> Empty { [self.elements->size()=1] put() }"); // t8
//				break;
//			case "outputMutant08":
//				faultyTransition = "t7";
//				TRANSITIONS.put("t7", "Normal -> Normal { [(self.capacity>1) and (self.elements->size()>=1)] get() }"); // t7
//				break;
//			case "outputMutant09":
//				faultyTransition = "t7";
//				TRANSITIONS.put("t7", "Normal -> Normal { [(self.capacity>1) and (self.elements->size()>1)] put() }"); // t7
//				break;
//			case "outputMutant10":
//				faultyTransition = "t7";
//				TRANSITIONS.put("t7", "Normal -> Normal { [(self.capacity=1) and (self.elements->size()>1)] get() }"); // t7
//				break;
//			case "outputMutant11":
//				faultyTransition = "t1";
//				TRANSITIONS.put("t1", "Empty -> Normal { [self.capacity=1] put() }"); // t1
//				break;
//			case "outputMutant13":
//				faultyTransition = "t3";
//				TRANSITIONS.put("t3",
//						"Normal -> Full { [((self.capacity > 1) and (self.elements->size = (self.capacity)))] put(p : Element) }"); // t3
//				break;
//			case "outputMutant14":
//				faultyTransition = "t3";
//				TRANSITIONS.put("t3",
//						"Normal -> Full { [((self.capacity > 1) and (self.elements->size = (self.capacity - 1)))] get(p : Element) }"); // t3
//				break;
//			case "outputMutant15":
//				faultyTransition = "t6";
//				TRANSITIONS.put("t6", "Full -> Normal { [self.capacity>=1] get() }"); // t6
//				break;
//			case "outputMutant16":
//				faultyTransition = "t6";
//				TRANSITIONS.put("t6", "Full -> Normal { [self.capacity=1] get() }"); // t6
//				break;
//			case "outputMutant18":
//				faultyTransition = "t5";
//				TRANSITIONS.put("t5", "Full -> Empty { [self.capacity>1] get() }"); // t5
//				break;
//			case "outputMutant20":
//				faultyTransition = "t4";
//				TRANSITIONS.put("t4", "Empty -> Full { [self.capacity>1] put() }"); // t4
//				break;
//			case "outputMutant21":
//				faultyTransition = "t1";
//				TRANSITIONS.put(); // t1
//				break;
//			default:
//				continue;
//			}

			// SBFL_TABLE.put(file.getName().replaceFirst("[.][^.]+$", ""), new
			// HashMap<String, Boolean>());

			try {
				// Read current mutant text file
				String mutantFileContent = Files.readString(Path.of(file.getPath()));

				// Fetch test cases
				List<String> testCases = new ArrayList<String>();
				testCases.addAll(Arrays.asList(mutantFileContent.split("TestCases.soil> -- ")));
				testCases.remove(0);

				for (String testCase : testCases) {
					HashMap<String, Boolean> testCaseResult = new HashMap<>();
					ArrayList<String> participatedTransisions = new ArrayList<String>();

					// String titlePrefix = "use> TestCases.soil> -- *******************************
					// ";
					String titleLine = testCase.split(System.lineSeparator())[0];
					String testCaseName = titleLine.replace("*", "").trim();

					// Get tuples of current test case
					String[] tuples = testCase.split("-> Tuple");

					// Find the errors in tuples, if any
					ArrayList<String> vistiedTuples = new ArrayList<String>();
					boolean anyErrorFound = false;

					for (String tuple : tuples) {

						if (tuple.contains("ok=true")) {
							vistiedTuples.add(tuple);
							continue;
						} else {
							// vistiedTuples.add(tuple);
							anyErrorFound = true;
							break;
						}
					}

					// All the transitions were correct and result is true
					if (!anyErrorFound) {
						parseLastValidTuple(TRANSITIONS, participatedTransisions, vistiedTuples);

						// error vector
						testCaseResult.put("Test result", true);

						// Mark successfulTransitions in SBFL_TABLE
					} else // There is at least one faulty transition
					{
						// error vector
						testCaseResult.put("Test result", false);

						// Parse Valid States
						parseLastValidTuple(TRANSITIONS, participatedTransisions, vistiedTuples);

						// Check for Possible Transitions
						String errornousTupple = vistiedTuples.get(vistiedTuples.size() - 1).replace(" ", "");
						if (errornousTupple.contains("Possibletransitions")) {
							String[] splitedTuple = errornousTupple.split("Possibletransitions");
							if (splitedTuple.length > 1) {
								String toParseTransitionLine = splitedTuple[1].split(System.lineSeparator())[0]
										.replace(" ", "");

								for (java.util.Map.Entry<String, String> e : TRANSITIONS.entrySet()) {

									String getKeyBytesUTF = e.getValue().replace(" ", "").replace(" ", "");
									String temp[] = getKeyBytesUTF.split("\\{");
									if (!getKeyBytesUTF.contains("{"))
										continue;

									getKeyBytesUTF = getKeyBytesUTF.split("\\{")[1].replace("}", "");

									System.out.println("*** error parse");
									System.out.println("Line: " + toParseTransitionLine);
									System.out.println("Current to compare: " + getKeyBytesUTF);
									if (toParseTransitionLine.indexOf(getKeyBytesUTF) > -1 // if it is
//											&& !faultyTransitions.contains(e.getKey()) // it is not mutant
											&& !participatedTransisions.contains(e.getKey())) {
										participatedTransisions.add(e.getKey());
									}
								}
							}
						} else if (errornousTupple.contains(
								"Error: No valid transition available in protocol state machine".replace(" ", ""))) {
							// Mart all available transitions from last state we are
							// Remove Prefix from Sequence
							String noTransitionPrefix = "{ok=true,oclStateTrace=Sequence{";
							String noPrefixStrForNoError = errornousTupple.substring(
									errornousTupple.indexOf(noTransitionPrefix) + noTransitionPrefix.length());
							String[] remainTextForNoError = noPrefixStrForNoError.split("}}");
							if (remainTextForNoError.length > 1) {
								String lastNoErrorSequence[] = remainTextForNoError[0].replace("'", "").replace("[", "")
										.split(",");
								String currentState = lastNoErrorSequence[lastNoErrorSequence.length - 1];

								for (java.util.Map.Entry<String, String> e : TRANSITIONS.entrySet()) {

									String getKeyBytesUTF = e.getValue().replace(" ", "").replace(" ", "")
											.toLowerCase();
									if (getKeyBytesUTF.indexOf(currentState.trim().toLowerCase() + "->") > -1
											&& !participatedTransisions.contains(e.getKey())) {
										participatedTransisions.add(e.getKey());
									}
								}
							}
						}
					}

					// Fill table column for specific test case

					for (java.util.Map.Entry<String, String> e : TRANSITIONS.entrySet()) {
						if (participatedTransisions.contains(e.getKey()))
							testCaseResult.put(e.getKey(), true);
						else
							testCaseResult.put(e.getKey(), false);
					}

					// bind the column o final SBFL_TABLE
					SBFL_TABLE.put(file.getName().replaceFirst("[.][^.]+$", "") + "-" + testCaseName, testCaseResult);
				}
//				int x = 5;

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Step 2: Calculate parameter values from SBFL_TABLE for current Mutant
			Map<String, HashMap<String, Boolean>> currentSBFL = SBFL_TABLE.entrySet().stream()
					.filter(p -> p.getKey().contains(file.getName().replaceFirst("[.][^.]+$", "")))
					.collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
			HashMap<String, HashMap<String, Double>> currentParameterTable = CalculateParameters(currentSBFL);

			// Add currentParameterTable to totalParameterTable Meysam
			HashMap<String, HashMap<String, Double>> currentAlgorithmTable = CalculateAlgorithms(currentParameterTable);
			// Add currentAlgorithmTable to PARAMETER_TABLE
			PARAMETER_TABLE.put(file.getName().replaceFirst("[.][^.]+$", ""), currentAlgorithmTable);

			MUTATION_ALGORITHMS.put(file.getName().replaceFirst("[.][^.]+$", ""),
					CalculateExamScore(currentAlgorithmTable));
			// double examScore = CalculateExamScore(currentAlgorithmTable);
			// Add currentAlgorithmTable to EXAMSCORE_TABLE
			// EXAMSCORE_TABLE.put(file.getName().replaceFirst("[.][^.]+$", ""), examScore);
		}

		// Append Score Avg to MUTATION_ALGORITHMS
		HashMap<String, Double> AvgScoreRow = new HashMap<>();
		for (Entry<String, HashMap<String, Double>> tmpRow : MUTATION_ALGORITHMS.entrySet()) {
			for (Entry<String, Double> param : tmpRow.getValue().entrySet()) {
				AvgScoreRow.put(param.getKey(), 0D);
			}
			break;
		}

		for (Entry<String, Double> param : AvgScoreRow.entrySet()) {
			for (Entry<String, HashMap<String, Double>> mutant : MUTATION_ALGORITHMS.entrySet()) {
				AvgScoreRow.put(param.getKey(), param.getValue() + mutant.getValue().get(param.getKey()));
			}
			AvgScoreRow.put(param.getKey(), param.getValue() / MUTATION_ALGORITHMS.size());
		}
		// Create Excel

		CreateExcelReport(SBFL_TABLE, PARAMETER_TABLE, AvgScoreRow);
	}

	private static void CreateExcelReport(TreeMap<String, HashMap<String, Boolean>> sbflTable,
			HashMap<String, HashMap<String, HashMap<String, Double>>> parameterTable,
			HashMap<String, Double> examSummary) {

		Workbook wb = new HSSFWorkbook();
		CreationHelper createHelper = wb.getCreationHelper();

		CellStyle headerStyle = wb.createCellStyle();
		org.apache.poi.ss.usermodel.Font font = wb.createFont();
		headerStyle.setFillForegroundColor((short) 12);
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREEN.getIndex());
		headerStyle.setFillBackgroundColor(HSSFColor.HSSFColorPredefined.GREEN.getIndex());
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
		headerStyle.setFont(font);

		CellStyle errorVectorStyle = wb.createCellStyle();
//		errorVectorStyle.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.getIndex());
		errorVectorStyle.setFillForegroundColor((short) 12);
		errorVectorStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		errorVectorStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREEN.getIndex());
		errorVectorStyle.setFillBackgroundColor(HSSFColor.HSSFColorPredefined.GREEN.getIndex());
		errorVectorStyle.setAlignment(HorizontalAlignment.CENTER);
		errorVectorStyle.setFont(font);

		CellStyle transitionStyle = wb.createCellStyle();
//		org.apache.poi.ss.usermodel.Font font = wb.createFont();
		transitionStyle.setFillForegroundColor((short) 12);
		transitionStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		transitionStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());
		transitionStyle.setFillBackgroundColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());
		transitionStyle.setAlignment(HorizontalAlignment.CENTER);
		transitionStyle.setFont(font);

		CellStyle SummaryCellStyle = wb.createCellStyle();
//		org.apache.poi.ss.usermodel.Font font = wb.createFont();
		SummaryCellStyle.setFillForegroundColor((short) 12);
		SummaryCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		SummaryCellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
		SummaryCellStyle.setFillBackgroundColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
		SummaryCellStyle.setAlignment(HorizontalAlignment.CENTER);
		SummaryCellStyle.setFont(font);

		Cell currentCell;
		// Create Exam Score sheet
		org.apache.poi.ss.usermodel.Sheet examScoreSheet = wb.createSheet("EXAM Score");

		int examScoreRowId = 3;

		// Exam Score sheet - Header Row
		Row examScoreHeaderRow = examScoreSheet.createRow((short) 2);
//		currentCell = examScoreHeaderRow.createCell(1);
//		currentCell.setCellValue("AVG Case");
//		currentCell.setCellStyle(headerStyle);

		// MUTATION_ALGORITHMS
		for (Entry<String, HashMap<String, Double>> currnetMutant : MUTATION_ALGORITHMS.entrySet()) {

			// Exam Score sheet - Create row
			Row examScoreRow = examScoreSheet.createRow((short) examScoreRowId++);
			currentCell = examScoreRow.createCell(1);
			currentCell.setCellValue(currnetMutant.getKey());
			currentCell.setCellStyle(headerStyle);
			examScoreSheet.autoSizeColumn(1);

//			examScoreRow.createCell(2).setCellValue(currnetMutant.getValue());
			int colIndex = 2;			
			for (Entry<String, Double> parameters : currnetMutant.getValue().entrySet()) {
				currentCell = examScoreHeaderRow.createCell(colIndex);// .setCellValue(parameters.getKey());
				currentCell.setCellValue(parameters.getKey());
				currentCell.setCellStyle(headerStyle);
				examScoreSheet.autoSizeColumn(colIndex);
				examScoreRow.createCell(colIndex).setCellValue(parameters.getValue());
				examScoreSheet.autoSizeColumn(colIndex);
				colIndex++;
//				double sumOfExamScore = 0D;
//				// Exam Score sheet - Body
//				sumOfExamScore += parameters.getValue();
			}

			// Mutants
			Map<String, HashMap<String, Boolean>> currentSBFL = sbflTable.entrySet().stream()
					.filter(p -> p.getKey().contains(currnetMutant.getKey() + "-")).collect(Collectors
							.toMap(p -> p.getKey(), p -> p.getValue(), (oldValue, newValue) -> newValue, TreeMap::new));

			// Parameters
			Map<String, HashMap<String, HashMap<String, Double>>> currentParameters = parameterTable.entrySet().stream()
					.filter(p -> p.getKey().equals(currnetMutant.getKey()))
					.collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));

			// Create a sheet for current mutant
			org.apache.poi.ss.usermodel.Sheet currnetMutantSheet = wb.createSheet(currnetMutant.getKey());

			// Create SBFL table header Meysam
			int mutantRowId = 12;
			Row mutantHeaderRow = currnetMutantSheet.createRow((short) mutantRowId);
			mutantRowId++;
			Row mutantErrorVectorRow = currnetMutantSheet.createRow((short) mutantRowId + TRANSITIONS.size());

			for (Entry<String, String> transition : TRANSITIONS.entrySet()) {
				// Create SBFL table body
				// SBFL table header
				Row mutantBodyRow = currnetMutantSheet.createRow((short) mutantRowId++);

				int columnCounter = 2;
				// SBFL table body
				for (Entry<String, HashMap<String, Boolean>> testCase : currentSBFL.entrySet()) {
					String currentColumnName = testCase.getKey().split("-")[1].trim();
					currentCell = mutantHeaderRow.createCell(columnCounter);// .setCellValue(currentColumnName);
					currentCell.setCellValue(currentColumnName);
					currentCell.setCellStyle(headerStyle);

					currentCell = mutantBodyRow.createCell(1);// .setCellValue(transition.getKey());
					currentCell.setCellValue(transition.getKey());
					currentCell.setCellStyle(transitionStyle);

					String value = "";
					if (testCase.getValue().get(transition.getKey()) == true)
						value = "X";
					mutantBodyRow.createCell(columnCounter).setCellValue(value);
					currnetMutantSheet.autoSizeColumn(columnCounter);

					currentCell = mutantErrorVectorRow.createCell(1);// .setCellValue("Test result");
					currentCell.setCellValue("Test Result");
					currentCell.setCellStyle(SummaryCellStyle);

					currentCell = mutantErrorVectorRow.createCell(columnCounter++);
					currentCell.setCellValue(testCase.getValue().get("Test result"));
					currentCell.setCellStyle(errorVectorStyle);
					// .setCellValue(testCase.getValue().get("Test result"));
					currnetMutantSheet.autoSizeColumn(columnCounter);
				}

				currentCell = mutantErrorVectorRow.createCell(2 + currentSBFL.size());// .setCellValue("Exam Score");
				currentCell.setCellValue("Exam Score");
				currentCell.setCellStyle(SummaryCellStyle);

				columnCounter = 3 + currentSBFL.size(); // 13
				// Parameter table body
				for (Entry<String, HashMap<String, HashMap<String, Double>>> parameter : currentParameters.entrySet()) {

					// Parameter table body
					for (Entry<String, Double> params : parameter.getValue().get(transition.getKey()).entrySet()) {
						currentCell = mutantHeaderRow.createCell(columnCounter);// .setCellValue(params.getKey());
						currentCell.setCellValue(params.getKey());
						currentCell.setCellStyle(headerStyle);

						mutantBodyRow.createCell(columnCounter).setCellValue(params.getValue());

						currentCell = mutantErrorVectorRow.createCell(columnCounter++);
//								.setCellValue(MUTATION_ALGORITHMS.get(parameter.getKey()).get(params.getKey()));
						currentCell.setCellValue(MUTATION_ALGORITHMS.get(parameter.getKey()).get(params.getKey()));
						currentCell.setCellStyle(errorVectorStyle);
						currnetMutantSheet.autoSizeColumn(columnCounter);
					}
				}
			}
		}

		// Exam Score sheet - Final Row
		Integer summaryRowIndex = 3 + parameterTable.size();
		Row examScoreSummaryRow = examScoreSheet.createRow(summaryRowIndex);
		currentCell = examScoreSummaryRow.createCell(1);
		currentCell.setCellValue("AVG EXAM");
		currentCell.setCellStyle(SummaryCellStyle);

		int colIndex = 2;
		for (Entry<String, Double> parameters : examSummary.entrySet()) {
			currentCell = examScoreSummaryRow.createCell(colIndex);// .setCellValue(parameters.getKey());
			currentCell.setCellValue(parameters.getValue());
			currentCell.setCellStyle(SummaryCellStyle);
			examScoreSheet.autoSizeColumn(colIndex);
			colIndex++;
		}
		
//		examScoreSummaryRow.createCell(2).setCellValue(sumOfExamScore / examScoreTable.size());

//		org.apache.poi.ss.usermodel.Sheet sheet = wb.createSheet("new sheet");
//
//		// Create a row and put some cells in it. Rows are 0 based.
//		Row row = sheet.createRow((short)0);
//		// Create a cell and put a value in it.
//		Cell cell = row.createCell(0);
//		cell.setCellValue(1);
//
//		// Or do it on one line.
//		row.createCell(1).setCellValue(1.2);
//		row.createCell(2).setCellValue(
//		createHelper.createRichTextString("This is a string"));
//		row.createCell(3).setCellValue(true);

		// Write the output to a file
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream("result.xls");
			wb.write(fileOut);
			fileOut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static HashMap<String, Double> CalculateExamScore(
			HashMap<String, HashMap<String, Double>> currentAlgorithmTable) {
		HashMap<String, Double> result = new HashMap<String, Double>(); // Cohen, 1, kentz, 2, ..., Rog, 3>\
		HashMap<String, ArrayList<Double>> innerResult = new HashMap<String, ArrayList<Double>>(); // Cohen, []
		HashMap<String, Double> faultyScores = new HashMap<String, Double>(); // Cohen, 4

		ArrayList<Double> Kulczynski2List = new ArrayList<>();
		double[] Kulczynski2Vector = new double[currentAlgorithmTable.size()];
		double faultyScore = 0;

		int i = 0;
		for (Entry<String, HashMap<String, Double>> col : currentAlgorithmTable.entrySet()) {
			for (Entry<String, Double> algorithm : col.getValue().entrySet()) {
				algorithm.getKey(); // cohen
				algorithm.getValue(); // 4

				if (!innerResult.containsKey(algorithm.getKey()))
					innerResult.put(algorithm.getKey(), new ArrayList<Double>());

				ArrayList<Double> currentAlgorithmScore = innerResult.get(algorithm.getKey());
				currentAlgorithmScore.add(algorithm.getValue());

				// if (col.getKey() == faultyTransition)
				if (faultyTransitions.contains(col.getKey()))
					faultyScores.put(algorithm.getKey(), algorithm.getValue());

//				Kulczynski2Vector[i++] = algorithm.getValue();
//				if (col.getKey() == faultyTransition)
//					faultyScore = col.getValue().get(algorithm.getKey());
			}

//			Kulczynski2Vector[i++] = col.getValue().get("Kulczynski2");
//			if (col.getKey() == faultyTransition)
//				faultyScore = col.getValue().get("Kulczynski2");
		}

		for (Entry<String, ArrayList<Double>> parameter : innerResult.entrySet()) {
			int tieSize = 0;
			int tieSum = 0;

			ArrayList<Double> currentArray = parameter.getValue();
			Collections.sort(currentArray);
			Collections.reverse(currentArray);

			for (int j = 0; j < currentArray.size(); j++) {
				if (currentArray.get(j) == faultyScores.get(parameter.getKey())) {
					tieSize++;
					tieSum += j + 1;
				}
			}
			result.put(parameter.getKey(), ((double) tieSum / tieSize) / parameter.getValue().size());
		}

		return result;// ((double) tieSum / tieSize) / Kulczynski2Vector.length;

	}

	private static HashMap<String, HashMap<String, Double>> CalculateAlgorithms(
			HashMap<String, HashMap<String, Double>> currentParameterTable) {
		HashMap<String, HashMap<String, Double>> ALGORITHM_TABLE = new HashMap<String, HashMap<String, Double>>();

		for (Entry<String, HashMap<String, Double>> row : currentParameterTable.entrySet()) {
			HashMap<String, Double> rowValues = new HashMap<String, Double>();

			// row.getValue().get("Ncf")
			// row.getValue().get("Nuf")
			// row.getValue().get("Ncs")
			// row.getValue().get("Nf")
			// row.getValue().get("Nus")
			// row.getValue().get("Ns")

			double L13 = row.getValue().get("Ncf");
			double M13 = row.getValue().get("Nuf");
			double N13 = row.getValue().get("Ncs");

			if (row.getValue().get("Ncf") + row.getValue().get("Nuf") == 0
					|| row.getValue().get("Ncf") + row.getValue().get("Ncs") == 0) {
				rowValues.put("Kulczynski2", 0D);
			} else {
//				double Kulczynski2 = (((double)1 / 2) * ((row.getValue().get("Ncf") / (row.getValue().get("Ncf") + row.getValue().get("Nuf")))
//						+ (row.getValue().get("Ncf") / (row.getValue().get("Ncf") + row.getValue().get("Ncs")))));
				double Kulczynski2 = (1D / 2) * (L13 / (L13 + M13) + (L13 / (L13 + N13)));
				rowValues.put("Kulczynski2", Kulczynski2);
			}

			if (row.getValue().get("Ncf") + row.getValue().get("Ncs") == 0) {
				rowValues.put("Ochiai", (double) 0);
			} else {
				double Ochiai = (double) row.getValue().get("Ncf") / (Math
						.sqrt(row.getValue().get("Nf") * (row.getValue().get("Ncf") + row.getValue().get("Ncs"))));
				rowValues.put("Ochiai", Ochiai);
			}

			if ((row.getValue().get("Ncf") + row.getValue().get("Ncs"))
					* (row.getValue().get("Nus") + row.getValue().get("Nuf"))
					* (row.getValue().get("Ncf") + row.getValue().get("Nuf"))
					* (row.getValue().get("Ncs") + row.getValue().get("Nus")) == 0) {
				rowValues.put("Ochiai2", (double) 0);
			} else {
				double Ochiai2 = (double) (row.getValue().get("Ncf") * row.getValue().get("Nus"))
						/ Math.sqrt((row.getValue().get("Ncf") + row.getValue().get("Ncs"))
								* (row.getValue().get("Nus") + row.getValue().get("Nuf"))
								* (row.getValue().get("Ncf") + row.getValue().get("Nuf"))
								* (row.getValue().get("Ncs") + row.getValue().get("Nus")));
				rowValues.put("Ochiai2", Ochiai2);
			}

			double Mountford = (double) row.getValue().get("Ncf") / 0.5
					* ((row.getValue().get("Ncf") * row.getValue().get("Ncs"))
							+ (row.getValue().get("Ncf") * row.getValue().get("Nuf")))
					+ (row.getValue().get("Ncs") * row.getValue().get("Nuf"));
			rowValues.put("Mountford", Mountford);

			if (row.getValue().get("Ncf") == 0) {
				rowValues.put("Zoltar", (double) 0);
			} else {
				double Zoltar = (double) row.getValue().get("Ncf") / (row.getValue().get("Ncf")
						+ row.getValue().get("Nuf") + row.getValue().get("Ncs")
						+ (10000 * row.getValue().get("Nuf") * row.getValue().get("Ncs") / row.getValue().get("Ncf")));
				rowValues.put("Zoltar", Zoltar);
			}

			if (row.getValue().get("Nf") == 0 || ((row.getValue().get("Ncf") / row.getValue().get("Nf"))
					+ (row.getValue().get("Ncs") / row.getValue().get("Ns"))) == 0) {
				rowValues.put("Tarantula", (double) 0);
			} else {
				double Tarantula = (double) (row.getValue().get("Ncf") / row.getValue().get("Nf"))
						/ ((row.getValue().get("Ncf") / row.getValue().get("Nf"))
								+ (row.getValue().get("Ncs") / row.getValue().get("Ns")));
				rowValues.put("Tarantula", Tarantula);
			}

			if (((row.getValue().get("Ncf") + row.getValue().get("Ncs"))
					* (row.getValue().get("Nus") + row.getValue().get("Nuf"))
					+ (row.getValue().get("Ncf") + row.getValue().get("Nuf"))
							* (row.getValue().get("Ncs") + row.getValue().get("Nus"))) == 0) {
				rowValues.put("ArithMean", (double) 0);
			} else {
				double ArithMean = (double) 2
						* (row.getValue().get("Ncf") * row.getValue().get("Nus")
								- row.getValue().get("Nuf") * row.getValue().get("Ncs"))
						/ ((row.getValue().get("Ncf") + row.getValue().get("Ncs"))
								* (row.getValue().get("Nus") + row.getValue().get("Nuf"))
								+ (row.getValue().get("Ncf") + row.getValue().get("Nuf"))
										* (row.getValue().get("Ncs") + row.getValue().get("Nus")));
				rowValues.put("ArithMean", ArithMean);
			}

			if ((row.getValue().get("Ncs") + row.getValue().get("Ncf")) == 0) {
				rowValues.put("Barinel", (double) 0);
			} else {
				double Barinel = (double) 1
						- (row.getValue().get("Ncs") / (row.getValue().get("Ncs") + row.getValue().get("Ncf")));
				rowValues.put("Barinel", Barinel);
			}

			double BU = (double) (Math.sqrt(row.getValue().get("Ncf") * row.getValue().get("Nus"))
					+ row.getValue().get("Ncf"))
					/ (Math.sqrt(row.getValue().get("Ncf") * row.getValue().get("Nus")) + row.getValue().get("Ncf")
							+ row.getValue().get("Ncs") + row.getValue().get("Nuf"));
			rowValues.put("B-U & Buser", BU);

			double BR = (double) row.getValue().get("Ncf")
					/ (Math.max(row.getValue().get("Ncf") + row.getValue().get("Ncs"),
							row.getValue().get("Ncf") + row.getValue().get("Nuf")));
			rowValues.put("Br-Banquet", BR);

			double Cohen = (double) 2
					* (row.getValue().get("Ncf") * row.getValue().get("Nus")
							- row.getValue().get("Nuf") * row.getValue().get("Ncs"))
					/ ((row.getValue().get("Ncf") + row.getValue().get("Ncs"))
							* (row.getValue().get("Nus") * row.getValue().get("Ncs"))
							+ (row.getValue().get("Ncf") + row.getValue().get("Nuf"))
									* (row.getValue().get("Nuf") + row.getValue().get("Nus")));
			rowValues.put("Cohen", Cohen);

			double D2 = Math.pow(row.getValue().get("Ncf"), 2)
					/ (row.getValue().get("Ncs") + row.getValue().get("Nf") + row.getValue().get("Ncf"));
			rowValues.put("D2", D2);

			double Op2 = (double) row.getValue().get("Ncf")
					- (row.getValue().get("Ncs") / (row.getValue().get("Ns") + 1));
			rowValues.put("Op2", Op2);

			if ((row.getValue().get("Ncf") + row.getValue().get("Ncs"))
					* (row.getValue().get("Ncf") + row.getValue().get("Nuf"))
					* (row.getValue().get("Ncs") + row.getValue().get("Nus"))
					* (row.getValue().get("Nuf") + row.getValue().get("Nus")) == 0) {
				rowValues.put("Phi", (double) 0);
			} else {
				double Phi = (double) (row.getValue().get("Ncf") * row.getValue().get("Nus")
						- row.getValue().get("Nuf") * row.getValue().get("Ncs"))
						/ (Math.sqrt((row.getValue().get("Ncf") + row.getValue().get("Ncs"))
								* (row.getValue().get("Ncf") + row.getValue().get("Nuf"))
								* (row.getValue().get("Ncs") + row.getValue().get("Nus"))
								* (row.getValue().get("Nuf") + row.getValue().get("Nus"))));
				rowValues.put("Phi", Phi);
			}

			if (((row.getValue().get("Ncf") * row.getValue().get("Nuf"))
					+ (2 * row.getValue().get("Nuf") * row.getValue().get("Nus"))
					+ (row.getValue().get("Ncs") * row.getValue().get("Nus"))) == 0) {
				rowValues.put("Pierce", (double) 0);
			} else {
				double Pierce = (double) ((row.getValue().get("Ncf") * row.getValue().get("Nuf"))
						+ (row.getValue().get("Nuf") * row.getValue().get("Ncs")))
						/ ((row.getValue().get("Ncf") * row.getValue().get("Nuf"))
								+ (2 * row.getValue().get("Nuf") * row.getValue().get("Nus"))
								+ (row.getValue().get("Ncs") * row.getValue().get("Nus")));
				rowValues.put("Pierce", Pierce);
			}

			double Rog = (double) (row.getValue().get("Ncf") + row.getValue().get("Nus")) / (row.getValue().get("Ncf")
					+ row.getValue().get("Nus") + 2 * (row.getValue().get("Nuf") + row.getValue().get("Ncs")));
			rowValues.put("Rog & Tan", Rog);

			double Russ = (double) row.getValue().get("Ncf") / (row.getValue().get("Ncf") + row.getValue().get("Nuf")
					+ row.getValue().get("Ncs") + row.getValue().get("Nus"));
			rowValues.put("Russ-Rao", Russ);

			double Simp = (double) (row.getValue().get("Ncf") + row.getValue().get("Nus")) / (row.getValue().get("Ncf")
					+ row.getValue().get("Nuf") + row.getValue().get("Ncs") + row.getValue().get("Nus"));
			rowValues.put("Simp. Match", Simp);

			ALGORITHM_TABLE.put(row.getKey(), rowValues);
		}
		return ALGORITHM_TABLE;

	}

	private static HashMap<String, HashMap<String, Double>> CalculateParameters(
			Map<String, HashMap<String, Boolean>> currentSBFL) {
		HashMap<String, HashMap<String, Integer>> PARAMETER_TABLE = new HashMap<String, HashMap<String, Integer>>();

		int Ns = 0; // total number of successful test cases
		int Nf = 0; // total number of failed test cases

		for (java.util.Map.Entry<String, HashMap<String, Boolean>> testCase : currentSBFL.entrySet()) { // testcase 8
			HashMap<String, Integer> currentMutantParameterTable = new HashMap<String, Integer>(); // Parameter Matrix
																									// to be calculated

			Boolean currentTestCaseTestResult = true;

			for (java.util.Map.Entry<String, Boolean> cell : testCase.getValue().entrySet()) { // SBFL_Table for currnet
																								// Mutant File
																								// (OuputMutant1)
				if (cell.getKey().contains("Test result")) {
					currentTestCaseTestResult = cell.getValue();
					if (currentTestCaseTestResult == true) {
						Ns++;
					} else {
						Nf++;
					}
					break;
				}
			}

//			int Ncs = 0; // number of successful test cases that trigger a transition
//			int Nus = 0; // number of successful test cases that do not triger a transition
//			int Ncf = 0; // number of failed test cases that trigger a transition
//			int Nuf = 0; // number of failed test cases that do not trigger a transition
//			int Nc = 0; // total number of test cases that trigger a transition
//			int Nu = 0; // total number of test cases that do not trigger a transition

			for (java.util.Map.Entry<String, Boolean> cell : testCase.getValue().entrySet()) { // SBFL_Table for currnet
																								// Mutant File
																								// (OuputMutant1)

				if (cell.getKey().contains("Test result")) {
					continue;
				}

				// Ncf number of failed test cases that trigger a transition
				HashMap<String, Double> currentTransition = TRANSITIONSParameterValues.get(cell.getKey()); // t4

				if (currentTestCaseTestResult == true) {

					currentTransition.put("Ns", currentTransition.get("Ns") + 1);

					if (cell.getValue() == true) {
						// Ncs++;
						currentTransition.put("Ncs", currentTransition.get("Ncs") + 1);
						// Nc++;
						currentTransition.put("Nc", currentTransition.get("Nc") + 1);
					} else {
						// Nus++;
						currentTransition.put("Nus", currentTransition.get("Nus") + 1);
						// ++;
						currentTransition.put("Nu", currentTransition.get("Nu") + 1);
					}

				} else {

					currentTransition.put("Nf", currentTransition.get("Nf") + 1);

					if (cell.getValue() == true) {
						// Ncf++;
						currentTransition.put("Ncf", currentTransition.get("Ncf") + 1);
						// Nc++;
						currentTransition.put("Nc", currentTransition.get("Nc") + 1);
					} else {
						// Nuf++;
						currentTransition.put("Nuf", currentTransition.get("Nuf") + 1);
						// Nu++;
						currentTransition.put("Nu", currentTransition.get("Nu") + 1);
					}

				}

			}
			PARAMETER_TABLE.put(testCase.getKey(), currentMutantParameterTable);
			// e.getKey()
			// e.getValue()

		}

		// return PARAMETER_TABLE;
		return TRANSITIONSParameterValues;
	}

	private static void ResetTransitionToDefaultState(String mutantName) {

		File file = new File("mutants/transitions.txt");
		try {
			// Read current mutant text file
			// String transitionFileContent =
			// Files.readString(Path.of(file.getPath())).trim();
			// String[] fileStructure = transitionFileContent.split("***");
			boolean isCurrentMutantFile = false;
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String line;
				boolean isOriginal = false;
				while ((line = br.readLine()) != null) {
					// process the line.
					if (line.trim().toUpperCase().replace(" ", "").replace(" ", "")
							.indexOf("ORIGINAL_TRANSITIONS:") > -1) {
						isOriginal = true;
						continue;
					} else if (line.trim().toUpperCase().replace(" ", "").replace(" ", "")
							.indexOf("MUTANT_TRANSITIONS:") > -1) {
						isOriginal = false;
						continue;
					} else if (line.trim().toUpperCase().replace(" ", "").replace(" ", "").indexOf("***") > -1)
						continue;

					if (isOriginal) {
						String[] originalLine = line.split(",");
						TRANSITIONS.put(originalLine[0].trim(), originalLine[1].trim());
						TRANSITIONSParameterValues.put(originalLine[0].trim(), new HashMap<String, Double>());
					} else {
						if (line.trim().endsWith(":")) {
							if (line.replace(":", "").indexOf(mutantName) > -1)
								isCurrentMutantFile = true;
							else
								isCurrentMutantFile = false;
						} else if (isCurrentMutantFile) {
							String[] mutantLine = line.split(",");
							TRANSITIONS.put(mutantLine[0], mutantLine[1]);
							if (!faultyTransitions.contains(mutantLine[0]))
								faultyTransitions.add(mutantLine[0]);
						}
					}
				}
			}

		} catch (Exception e) {
			int x = 5;
		}

////		TRANSITIONS.put("t0", " s -> Empty"); // t0
//		TRANSITIONS.put("t1", "Empty -> Normal { [self.capacity>1] put() }"); // t1
//
////		TRANSITIONS.put("t2", "Normal -> Normal { [self.elements->size()<capacity-1] put() }"); // t2	
//		TRANSITIONS.put("t2", "Normal -> Normal { [(self.elements->size < (self.capacity - 1))] put(p : Element) }"); // t2
//
//		// TRANSITIONS.put("t3", "Normal -> Full { [(self.capacity>1) and
//		// (self.elements->size()=capacity-1)] put() }"); // t3
//		TRANSITIONS.put("t3",
//				"Normal -> Full { [((self.capacity > 1) and (self.elements->size = (self.capacity - 1)))] put(p : Element) }"); // t3
//
//		TRANSITIONS.put("t4", "Empty -> Full { [self.capacity=1] put() }"); // t4
//		TRANSITIONS.put("t5", "Full -> Empty { [self.capacity=1] get() }"); // t5
//		TRANSITIONS.put("t6", "Full -> Normal { [self.capacity>1] get() }"); // t6
//		TRANSITIONS.put("t7", "Normal -> Normal { [(self.capacity>1) and (self.elements->size()>1)] get() }"); // t7
//		TRANSITIONS.put("t8", "Normal -> Empty { [self.elements->size()=1] get() }"); // t8

//		TRANSITIONSParameterValues.put("t0", new HashMap<String, Integer>());
//		TRANSITIONSParameterValues.put("t1", new HashMap<String, Double>());
//		TRANSITIONSParameterValues.put("t2", new HashMap<String, Double>());
//		TRANSITIONSParameterValues.put("t3", new HashMap<String, Double>());
//		TRANSITIONSParameterValues.put("t4", new HashMap<String, Double>());
//		TRANSITIONSParameterValues.put("t5", new HashMap<String, Double>());
//		TRANSITIONSParameterValues.put("t6", new HashMap<String, Double>());
//		TRANSITIONSParameterValues.put("t7", new HashMap<String, Double>());
//		TRANSITIONSParameterValues.put("t8", new HashMap<String, Double>());

		for (Entry<String, HashMap<String, Double>> e : TRANSITIONSParameterValues.entrySet()) {
			HashMap<String, Double> current = e.getValue();
			current.put("Ncf", 0D);
			current.put("Nuf", 0D);
			current.put("Ncs", 0D);
			current.put("Nus", 0D);
			current.put("Nc", 0D);
			current.put("Nu", 0D);
			current.put("Ns", 0D);
			current.put("Nf", 0D);
		}

	}

	private static void parseLastValidTuple(TreeMap<String, String> MUTANTS, ArrayList<String> participatedTransisions,
			ArrayList<String> vistiedTuples) {
		// Get last sequence of actions
		String lastSequeceOfSuccessfulActions = vistiedTuples.get(vistiedTuples.size() - 1);

		// Extract the sequenceOfActions
		// Remove Prefix from Sequence
		String prefix = "{ok=true,oclStateTrace=Sequence{";
		String noPrefixStr = lastSequeceOfSuccessfulActions
				.substring(lastSequeceOfSuccessfulActions.indexOf(prefix) + prefix.length());
		// Remove Postfix from Sequence
		String postFix = "}} : Tuple";
		// String theSequenceOfActions = noPrefixStr.split("}}")[0];
		String theSequenceOfActionsStr = noPrefixStr.substring(0, noPrefixStr.indexOf(postFix));

		// Parse successfulTransitions
		String[] theSequenceOfActions = theSequenceOfActionsStr.split(",");
		String searchingTransientClause = "";// theSequenceOfActions[0] + " -> " +
												// theSequenceOfActions[2];
		String actionClause = "";// theSequenceOfActions[1] + "() }";
		int currentState = 0;
		int finalState = theSequenceOfActions.length - 1;

		while (currentState < finalState) {

			searchingTransientClause = theSequenceOfActions[currentState].replace("'", "") + " -> "
					+ theSequenceOfActions[currentState + 2].replace("'", "");
			actionClause = theSequenceOfActions[currentState + 1].replace("'", "");// + " }";
			currentState += 2;

			System.out.println("*******searchingTransientClause: " + searchingTransientClause);
			System.out.println("******actionClause: " + actionClause);

			String searchingTransientClauseUTF = searchingTransientClause.replace(" ", "");
			String actionClauseBytesUTF = actionClause.replace(" ", "").replace("()", "");
//							byte[] actionClauseBytes = actionClause.getBytes(StandardCharsets.UTF_8);
//							String actionClauseBytesUTF = new String(actionClauseBytes, StandardCharsets.UTF_8);
//							
//							byte[] searchingTransientClauseBytes = searchingTransientClause.getBytes(StandardCharsets.UTF_8);
//							String searchingTransientClauseUTF = new String(searchingTransientClauseBytes, StandardCharsets.UTF_8);

			for (java.util.Map.Entry<String, String> e : MUTANTS.entrySet()) {
				System.out.println("e.getKey(): " + e.getValue());

				String getKeyBytesUTF = e.getValue().replace(" ", "").replace(" ", "");

				if (getKeyBytesUTF.indexOf(searchingTransientClauseUTF) > -1) {
					if (getKeyBytesUTF.indexOf(actionClauseBytesUTF.trim()) > -1
							&& !participatedTransisions.contains(e.getValue())) {
						participatedTransisions.add(e.getKey());
					}
				}
			}
		}
	}

	private static void parseLastValidTuple2(HashMap<String, String> MUTANTS, ArrayList<String> participatedTransisions,
			String lastSequeceOfSuccessfulActions) {
		// Get last sequence of actions
		// String lastSequeceOfSuccessfulActions =
		// vistiedTuples.get(vistiedTuples.size() - 1);

		// Extract the sequenceOfActions
		// Remove Prefix from Sequence
		String prefix = "{ok=true,oclStateTrace=Sequence{";
		String noPrefixStr = lastSequeceOfSuccessfulActions
				.substring(lastSequeceOfSuccessfulActions.indexOf(prefix) + prefix.length());
		// Remove Postfix from Sequence
		String postFix = "}} : Tuple";
		// String theSequenceOfActions = noPrefixStr.split("}}")[0];
		String theSequenceOfActionsStr = noPrefixStr.substring(0, noPrefixStr.indexOf(postFix));

		// Parse successfulTransitions
		String[] theSequenceOfActions = theSequenceOfActionsStr.split(",");
		String searchingTransientClause = "";// theSequenceOfActions[0] + " -> " +
												// theSequenceOfActions[2];
		String actionClause = "";// theSequenceOfActions[1] + "() }";
		int currentState = 0;
		int finalState = theSequenceOfActions.length - 1;

		while (currentState < finalState) {

			searchingTransientClause = theSequenceOfActions[currentState].replace("'", "") + " -> "
					+ theSequenceOfActions[currentState + 2].replace("'", "");
			actionClause = theSequenceOfActions[currentState + 1].replace("'", "");// + " }";
			currentState += 2;

			System.out.println("*******searchingTransientClause: " + searchingTransientClause);
			System.out.println("******actionClause: " + actionClause);

			String searchingTransientClauseUTF = searchingTransientClause.replace(" ", "");
			String actionClauseBytesUTF = actionClause.replace(" ", "").replace("()", "");
//							byte[] actionClauseBytes = actionClause.getBytes(StandardCharsets.UTF_8);
//							String actionClauseBytesUTF = new String(actionClauseBytes, StandardCharsets.UTF_8);
//							
//							byte[] searchingTransientClauseBytes = searchingTransientClause.getBytes(StandardCharsets.UTF_8);
//							String searchingTransientClauseUTF = new String(searchingTransientClauseBytes, StandardCharsets.UTF_8);

			for (java.util.Map.Entry<String, String> e : MUTANTS.entrySet()) {
				System.out.println("e.getKey(): " + e.getValue());

				String getKeyBytesUTF = e.getValue().replace(" ", "").replace(" ", "");

				if (getKeyBytesUTF.indexOf(searchingTransientClauseUTF) > -1) {
					if (getKeyBytesUTF.indexOf(actionClauseBytesUTF.trim()) > -1
							&& !participatedTransisions.contains(e.getValue())) {
						participatedTransisions.add(e.getKey());
					}
				}
			}
		}
	}
}
