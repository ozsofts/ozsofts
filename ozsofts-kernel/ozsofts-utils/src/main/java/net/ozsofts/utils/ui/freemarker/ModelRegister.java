package net.ozsofts.utils.ui.freemarker;

import java.util.HashMap;
import java.util.Map;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class ModelRegister {
	private Configuration cfg;

	public ModelRegister(Configuration cfg) {
		this.cfg = cfg;
	}

	public Configuration getConfiguration() {
		return cfg;
	}

	public void registerGlobalMethod(String key, Object target, String methodName) throws TemplateModelException {
		registerGlobalModel(key, useObjectMethod(target, methodName));
	}

	@SuppressWarnings("unchecked")
	public void registerGlobalModel(String key, Object target) throws TemplateModelException {
		String[] keys = key.split("\\.");

		if (keys.length < 1) {
			throw new IllegalArgumentException("Invalid model registration key: " + key);
		}

		BeansWrapper wrapper = (BeansWrapper) cfg.getObjectWrapper();

		TemplateModel model = cfg.getObjectWrapper().wrap(target);
		TemplateModel exist = cfg.getSharedVariable(keys[0]);
		if (exist == null) {
			cfg.setSharedVariable(keys[0], buildDeepTemplateMap(keys, 1, model));
		} else {
			Map<String, TemplateModel> registry = null;
			int i = 0;
			for (; i < keys.length - 1; i++) {
				if (exist == null) {
					registry.put(keys[i], buildDeepTemplateMap(keys, i + 1, model));
					return;
				}

				if (exist instanceof TemplateHashModel) {
					Object obj = wrapper.unwrap(exist);
					if (obj instanceof Map) {
						registry = (Map<String, TemplateModel>) obj;
						exist = registry.get(keys[i + 1]);
					}
				} else {
					throw new IllegalArgumentException("model '" + key + "' at '" + keys[i]
									+ "' already defined and can't be added because the existing type is '" + exist.getClass().getName());
				}
			}
			if (exist != null) {
				throw new IllegalArgumentException("model '" + key + "' at '" + keys[i] + "' has conflicting updates for '" + exist.toString()
								+ "' and '" + target.toString() + "'");
			}
			registry.put(keys[i], buildDeepTemplateMap(keys, i + 1, model));
		}
	}

	private TemplateModel buildDeepTemplateMap(String[] keys, int start, TemplateModel target) throws TemplateModelException {
		ObjectWrapper wrapper = cfg.getObjectWrapper();
		for (int i = keys.length - 1; i >= start; i--) {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put(keys[i], target);
			target = wrapper.wrap(model);
		}
		return target;
	}

	public TemplateModel useObjectModel(Object target) throws TemplateModelException {
		ObjectWrapper wrapper = cfg.getObjectWrapper();
		TemplateModel model = wrapper.wrap(target);
		return model;
	}

	public TemplateModel useObjectMethod(Object target, String methodName) throws TemplateModelException {
		TemplateHashModel model = (TemplateHashModel) useObjectModel(target);
		return model.get(methodName);
	}

	public TemplateModel useClass(String className) throws TemplateModelException {
		BeansWrapper wrapper = (BeansWrapper) cfg.getObjectWrapper();
		TemplateHashModel staticModels = wrapper.getStaticModels();
		return staticModels.get(className);
	}
}
