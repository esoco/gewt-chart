//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// This file is a part of the 'gewt-chart' project.
// Copyright 2017 Elmar Sonnenschein, esoco GmbH, Flensburg, Germany
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//	  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
package de.esoco.ewt.component;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

import com.google.gwt.dom.client.Element;


/********************************************************************
 * TODO: DOCUMENT ME!
 *
 * @author eso
 */
@JsType(isNative  = true, namespace = JsPackage.GLOBAL, name = "Object")
public class JsDatamapOptions
{
	//~ Instance fields --------------------------------------------------------

	/** Boolean: set responsive rendering */
	@JsProperty
	public boolean responsive;

	/** {@link Element}: the element to render the datamap in. */
	@JsProperty
	public Element element;
}