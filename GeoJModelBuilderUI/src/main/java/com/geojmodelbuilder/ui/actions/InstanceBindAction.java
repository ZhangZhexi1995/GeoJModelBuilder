/**
 * Copyright (C) 2013 - 2016 Wuhan University
 * 
 * This program is free software; you can redistribute and/or modify it under 
 * the terms of the GNU General Public License version 2 as published by the 
 * Free Software Foundation.
 * 
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 */
package com.geojmodelbuilder.ui.actions;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.geojmodelbuilder.ui.dialogs.InstanceManageDialog;
import com.geojmodelbuilder.ui.editparts.WorkflowProcessEditPart;
import com.geojmodelbuilder.ui.models.WorkflowProcess;
/**
 * 
 * @author Mingda Zhang
 *	not connect to a command. It should be.
 */
public class InstanceBindAction extends SelectionAction {

	public final static String REQUEST_NAME = "instance_add_request";
	public final static String REQEST_DATA_KEY = "workflow_process_instance";
	public final static String ID = "process_instance_add_id";

	private WorkflowProcess process;
	
	public InstanceBindAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(false);
		setId(ID);
	}

	@Override
	protected void init() {
		super.init();
		setText("Bind");
		setToolTipText("Bind new process instance");
		
		setId(ID);
		setEnabled(false);
	}
	
	@Override
	protected boolean calculateEnabled() {
		this.process = getSelectedEditPart();
		if(process == null)
			return false;
		
		return true;
	}

	@Override
	public void run() {
		super.run();
		
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
//		InstanceAddDialog dialog = new InstanceAddDialog(shell, this.process);
		InstanceManageDialog dialog = new InstanceManageDialog(shell, this.process);
		if(dialog.open() == Window.OK){
			System.out.println("update binding");
		}
	}

	private WorkflowProcess getSelectedEditPart() {
		List objects = getSelectedObjects();
		if (objects.size() == 0)
			return null;

		if (!(objects.get(0) instanceof EditPart))
			return null;

		EditPart editPart = (EditPart) objects.get(0);

		if (!(editPart instanceof WorkflowProcessEditPart))
			return null;

		
		return (WorkflowProcess)((WorkflowProcessEditPart) editPart).getModel();
	}
	
	@Override
	public String getId() {
		return ID;
	}
}
