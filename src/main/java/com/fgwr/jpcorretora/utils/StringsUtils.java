package com.fgwr.jpcorretora.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public class StringsUtils {

	public static String formatarCpf(String cpf) {
		StringBuilder sb = new StringBuilder(cpf);
		sb.insert(3, ".");
		sb.insert(7, ".");
		sb.insert(11, "-");
		return sb.toString();
	}

	public static String formatarTelefone(String telefone) {
		Integer count = telefone.length();
		StringBuilder sb = new StringBuilder(telefone);
		sb.insert(0, "(");
		sb.insert(3, ")");
		sb.insert(4, " ");
		sb = (count == 11) ? sb.insert(10, "-") : sb.insert(9, "-");
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
}
