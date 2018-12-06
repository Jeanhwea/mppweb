package net.jeanhwea.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import net.jeanhwea.ds.MyTask;

public class Statics {
	private float[][] 	duration; 
	private int[][] 	solution;
	private Vector<MyTask> v_task;
	private int			ntask;
	private int 		ndepd;
	private int 		nreso;
	
	private int 		npop;
	private int 		ngen;
	
	String path_to_output = "F:/HuJinghui/eclipse/output.txt";
	String path_to_result = "F:/HuJinghui/eclipse/result.txt";
	String path_to_xml 	  = "F:/HuJinghui/eclipse/workspace/mpp/WebContent/upload/";

	private DocumentBuilderFactory doc_builder_factory;
	private DocumentBuilder doc_builder;
	private Document doc;
	private float[] task_dura;
	private boolean[][] is_assign;

	
	public Statics(String xml_filename) 
	{
		path_to_output = path_to_output.replace('/', File.separatorChar);
		path_to_result = path_to_result.replace('/', File.separatorChar);
		path_to_xml = path_to_xml.replace('/', File.separatorChar) + xml_filename;
		npop = 200;
		ngen = 100;
		//System.out.println(xml_filename);
	}
	
	void loadDuration() 
			throws FileNotFoundException 
	{
		int i, j;
		duration = new float[ngen][npop];
		Scanner inputStream = new Scanner(new File(path_to_output));	
		for (i = 0; i < ngen; i++) {
			for (j = 0; j < npop; j++) {
				duration[i][j] = inputStream.nextFloat();
			}
		}
		inputStream.close();
	}
	
	void loadSolution() 
			throws FileNotFoundException
	{
		int i, j;

		solution = new int[3][ntask];

		Scanner inputStream = new Scanner(new File(path_to_result));
		for (i = 0; i < 3; i++) {
			for (j = 0; j < ntask; j++) {
				solution[i][j] = inputStream.nextInt();
			}
		}
		inputStream.close();
	}
	
	void loadTasks() 
	{
		Node size_tag = doc.getElementsByTagName("Size").item(0);
		NamedNodeMap attrs = size_tag.getAttributes();
		if (attrs != null) {
			Node nd;
			nd = attrs.getNamedItem("TaskSize");
			ntask = Integer.valueOf(nd.getNodeValue());
			nd = attrs.getNamedItem("ResourceSize");
			nreso = Integer.valueOf(nd.getNodeValue());
			nd = attrs.getNamedItem("DependSize");
			ndepd = Integer.valueOf(nd.getNodeValue());
		}
		
		task_dura = new float[ntask];
		is_assign = new boolean[ntask][nreso];
		NodeList task_tag = doc.getElementsByTagName("Task");
		for (int i = 0; i < task_tag.getLength(); i++) {
			attrs = task_tag.item(i).getAttributes();
			if (attrs != null) {
				Node nd; int idx; float du;
				nd = attrs.getNamedItem("id");
				idx = Integer.valueOf(nd.getNodeValue());
				nd = attrs.getNamedItem("duration");
				du = Float.valueOf(nd.getNodeValue());
				task_dura[idx-1] = du;
			}
		}
		NodeList asgn_tag = doc.getElementsByTagName("Assignment");
		for (int i = 0; i < asgn_tag.getLength(); i++) {
			attrs = asgn_tag.item(i).getAttributes();
			if (attrs != null) {
				Node nd; int tid, rid;
				nd = attrs.getNamedItem("task");
				tid = Integer.valueOf(nd.getNodeValue());
				nd = attrs.getNamedItem("resource");
				rid = Integer.valueOf(nd.getNodeValue());
				is_assign[tid-1][rid-1] = true;
			}
		}
	}

	@SuppressWarnings("unchecked")
	public JSONArray genAssignment(int solution_id)
	{
		JSONArray json;
		json = new JSONArray();
		int i, r, itask;
		float[] start;
		int[]   use_resid;
		float[] occupy;	

		start = new float[ntask];
		use_resid = new int[ntask];
		occupy = new float[nreso];
	    for (i = 0; i < ntask; i++) {
	        itask = solution[solution_id-1][i];
	        float dura = task_dura[itask-1];

	        int min_id = 0;
	        float min_occ = 0, occ;
	        for (r = 1; r <= nreso; r++) { // search all resources
	            if (is_assign[itask-1][r-1]) {
	                if (min_id == 0) {
	                    min_occ = occupy[r-1];
	                    min_id = r;
	                } else {
	                    occ = occupy[r-1];
	                    if (occ < min_occ) {
	                        min_occ = occ;
	                        min_id = r;
	                    }
	                }
	            }
	        }

	        if (min_id <= 0) {
	            min_id = 1;
	            min_occ = occupy[min_id-1];
	        }

            start[itask-1] = min_occ;
            use_resid[itask-1] = min_id; 
            occupy[min_id-1] += dura;
	    }	
	    
	    JSONObject jobj;
	    for (i = 0; i < ntask; i++) {
	    	jobj = new JSONObject();
	        itask = solution[solution_id-1][i];
	    	jobj.put("startDate", start[itask-1]);
	    	jobj.put("endDate", start[itask-1]+task_dura[itask-1]);
	    	jobj.put("taskName", "T"+itask);
	    	jobj.put("resoName", "R"+use_resid[itask-1]);
	    	json.add(jobj);
	    }
	    
	    
	    return json;
	}
	
	void loadXML()
			throws ParserConfigurationException, TransformerConfigurationException, SAXException, IOException
	{
		doc_builder_factory = DocumentBuilderFactory.newInstance();
		doc_builder = doc_builder_factory.newDocumentBuilder();
		doc = doc_builder.parse(new File(path_to_xml));
		
	}
	
	public void run()
			throws TransformerConfigurationException, ParserConfigurationException, SAXException, IOException
	{
		loadXML();
		loadTasks();
		loadDuration();
		loadSolution();
	}
	
	@SuppressWarnings("unchecked")
	public JSONArray genAerageDuration() 
	{
		JSONArray json, jmax, jmin, jmean;
		json = new JSONArray();
		jmax = new JSONArray();
		jmin = new JSONArray();
		jmean = new JSONArray();
		float[][] output = staDuration();
		
		for (int i = 0; i < ngen; i++) {
			jmax.add(output[0][i]);
			jmean.add(output[1][i]);
			jmin.add(output[2][i]);
		}
		
		
		JSONObject json_obj;
		json_obj = new JSONObject();
		json_obj.put("name", "Maximum");
		json_obj.put("data", jmax);
		json.add(json_obj);
		json_obj = new JSONObject();
		json_obj.put("name", "Minimum");
		json_obj.put("data", jmin);
		json.add(json_obj);
		json_obj = new JSONObject();
		json_obj.put("name", "Mean");
		json_obj.put("data", jmean);
		json.add(json_obj);
		
		// System.out.println(json);

		return json;
	}
	

	
	private float[][] staDuration()
	{
		float[][] ret = new float[3][ngen];
		float max, min, mean, sum;
		for (int i = 0; i < ngen; i++) {
			min = max = duration[i][0];
			sum = 0.0f;
			for (int j = 0; j < npop; j++) {
				if (duration[i][j] < min) {
					min = duration[i][j];
				}
				if (duration[i][j] > max) {
					max = duration[i][j];
				}
				sum += duration[i][j];
			}
			mean = sum / (float)npop;
			
			ret[0][i] = max;
			ret[1][i] = mean;
			ret[2][i] = min;
		}
		return ret;
	}

	public float[][] getDuration() 
	{
		return duration;
	}

	public void setDuration(float[][] duration) 
	{
		this.duration = duration;
	}

	public int[][] getSolution() 
	{
		return solution;
	}

	public void setSolution(int[][] solution) 
	{
		this.solution = solution;
	}

	public Vector<MyTask> getV_task() 
	{
		return v_task;
	}

	public void setV_task(Vector<MyTask> v_task) 
	{
		this.v_task = v_task;
	}

	public int getNtask() 
	{
		return ntask;
	}

	public void setNtask(int ntask) 
	{
		this.ntask = ntask;
	}

	public int getNdepd() 
	{
		return ndepd;
	}

	public void setNdepd(int ndepd) 
	{
		this.ndepd = ndepd;
	}

	public int getNreso() 
	{
		return nreso;
	}

	public void setNreso(int nreso) 
	{
		this.nreso = nreso;
	}
	
	
}
