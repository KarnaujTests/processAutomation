package com.juancarloscasas.baseProject.FrameworkTools.SeleniumExtensions;

public interface IParameterizedActions {
	
	/**
	 * Localiza la fila de la solicitud con el numero de solicitud indicado por el identifador provisto
	 * @param string Identificador de la solicitud que se espera encontrar
	 * @return Devuelve el numero de la fila en la que se encuentra la solicitud
	 */
	int LocalizarNumeroSolicitud(CustomDriver driver);
	
	//TODO: Si se cambia a JAVA 8 poner el metodo por defecto para evitar repeticion
//	public default void ClickEnAccionSolicitud(ParameterizedWebElement botonAccionParametrico) {
//		if (driverPruebas.getIdentificadorSolicitud() == null) {
//			Assert.fail("! ERROR: El numero de la solicitud ha de localizarse anteriormente a la ejecucion de este metodo.");
//		}
//
//		int filaSolicitud = LocalizarNumeroSolicitud(driverPruebas.getIdentificadorSolicitud());
//		
//		ClickConRefresco(botonAccionParametrico.getElement(Integer.toString(filaSolicitud)));
//	}
	/**
	 * Click en una de las acciones asociadas al numero de solicitud contenido en el driver de navegacion de la pagina.
	 * En primer lugar buscara el grupo de ordenes de la solicitud dentro de la pagina y posteriormente realizara un click con
	 * refresco sobre dicho elemento (comportamiento por defecto). Ver implementacion comentada en la interfaz
	 * @param botonAccionParametrico Objeto del elemento del driver con el que se desea interactuar
	 */
	void ClickEnAccionSolicitud(ParameterizedWebElement botonAccionParametrico);
	
	
}