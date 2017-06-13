/**
 * Copyright (C) 2013 - 2016 Wuhan University,
 *                           Center for Geographic Analysis, Harvard University
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

import java.io.File;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.geojmodelbuilder.semantic.serialization.Instance2RDF;
import org.geojmodelbuilder.semantic.serialization.Provenance2RDF;
import org.geojmodelbuilder.semantic.serialization.Template2RDF;

import com.geojmodelbuilder.core.instance.IWorkflowInstance;
import com.geojmodelbuilder.core.provenance.IWorkflowProv;
import com.geojmodelbuilder.ui.dialogs.WorkflowSaveDialog;
import com.geojmodelbuilder.ui.editors.ModelEditor;
import com.geojmodelbuilder.ui.models.Workflow;
import com.geojmodelbuilder.ui.run.UIModles2Instance;

/**
 * @author Mingda Zhang
 *
 */
public class WorkflowSaveAction extends Action{

	public static final String ID = "workflow.save";
	private IWorkbenchWindow window;
	public WorkflowSaveAction(IWorkbenchWindow window){
		this.window = window;
		this.setText("Save");
		this.setId(ID);
	}
	
	@Override
	public void run() {
		super.run();
		
		IEditorPart editorPart = window.getActivePage().getActiveEditor();
		if(!(editorPart instanceof ModelEditor))
			return;
		
		Workflow workflow = ((ModelEditor)editorPart).getWorkflow();
		
		if (workflow == null || workflow.getProcesses().size()==0) {
			MessageDialog.openWarning(window.getShell(), "Warning",
					"There is no process.");
			return;
		}
		
		WorkflowSaveDialog dialog = new WorkflowSaveDialog(window.getShell());
		
		if(dialog.open() != Window.OK)
			return;
		
		String path = dialog.getPath();
		boolean flag = false;
		
		if (dialog.isTemplate()) {
			Template2RDF template2rdf = new Template2RDF(workflow, true);
			flag = template2rdf.save(path);
		}
		
		
		File file = new File(path);
		String parentFile = file.getParent();
		String filename = file.getName();
		
		//save the instance to separate files.
		int numInstance = 0;
		if(dialog.isInstanceOf()){
			List<IWorkflowInstance> workflowInstances = workflow.getInstances();
			
			for(IWorkflowInstance instance:workflowInstances){
				numInstance ++;
				String idlfile = parentFile + File.separator + "Idl" + numInstance + "_" + filename;
				Instance2RDF instance2rdf = new Instance2RDF(instance);
				instance2rdf.save(idlfile);
			}
			
			if (workflowInstances == null || workflowInstances.size() == 0) {
				UIModles2Instance uiModles2Instance = new UIModles2Instance(workflow);
				IWorkflowInstance workflowInstance = uiModles2Instance.getExecutableWorkflow();
				if(workflowInstance != null){
					numInstance ++;
					String idlfile = parentFile + File.separator + "Instance_" + filename;
					Instance2RDF instance2rdf = new Instance2RDF(workflowInstance);
					instance2rdf.save(idlfile);
				}
			}
		}

		int numExecution = 0;
		if(dialog.isExecution()){
			for(IWorkflowProv workflowProv:workflow.getWorkflowProvs()){
				numExecution ++;
				Provenance2RDF provenance2rdf = new Provenance2RDF(workflowProv);
				String proFile = parentFile + File.separator + "Pro" + numExecution + "_" + filename;
				provenance2rdf.save(proFile);
			}
		}
		
		if(flag){
			MessageDialog.openInformation(window.getShell(), "Successfully", "Saved 1 template, "+ numInstance +" instance, " + numExecution + " execution.");
		}else {
			MessageDialog.openError(window.getShell(), "Message", "Failed to save the workflow.");
		}
		
	}
	
	
}
