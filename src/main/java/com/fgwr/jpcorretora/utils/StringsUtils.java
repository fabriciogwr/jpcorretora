package com.fgwr.jpcorretora.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringsUtils {

	public static String formatarCpfOuCnpj(String cpfOuCnpj) {
		StringBuilder sb = new StringBuilder(cpfOuCnpj);
		if (cpfOuCnpj.length() == 11) {
		sb.insert(3, ".");
		sb.insert(7, ".");
		sb.insert(11, "-");
		} else if (cpfOuCnpj.length() == 14) {
			sb.insert(2, ".");
			sb.insert(6, ".");
			sb.insert(9, "/");
			sb.insert(14, "-");			
		}
		return sb.toString();
	}

	public static String formatarTelefone(String telefone) {
		StringBuilder sb = new StringBuilder(telefone);
		sb.insert(0, "(");
		sb.insert(3, ")");
		sb.insert(4, " ");
		sb = (telefone.length() == 11) ? sb.insert(10, "-") : sb.insert(9, "-");
		return sb.toString();
	}
	
	public static String formatarDecimals(Double valor) {
	
	DecimalFormatSymbols separador = new DecimalFormatSymbols();
	separador.setDecimalSeparator('.');				
	DecimalFormat df = new DecimalFormat("0.00", separador);
	return df.format(valor);
	}
	
	public static String formatarReal(Double valor) {
	NumberFormat real = NumberFormat.getNumberInstance();
	real.setMinimumFractionDigits(2);
	real.setMaximumFractionDigits(2);
	return real.format(valor);
	}
	
	public static String formatarRealCifra(Double valor) {
		NumberFormat real = NumberFormat.getNumberInstance();
		real.setMinimumFractionDigits(2);
		real.setMaximumFractionDigits(2);
		return "R$"+real.format(valor);
		}
	
	public static String formatarCep(String cep) {
		StringBuilder sb = new StringBuilder(cep);
		sb.insert(5, "-");
		return sb.toString();
		}
	
	public static String formatarData(Date data) {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    	String dataString = df.format(data);
		return dataString;
	}
	
	public static String formatarDataBackup(Date data) {
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy_HH.mm");
    	String dataString = df.format(data);
		return dataString;
	}
}
