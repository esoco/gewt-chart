//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// This file is a part of the 'gewt-chart' project.
// Copyright 2016 Elmar Sonnenschein, esoco GmbH, Flensburg, Germany
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
package de.esoco.ewt.build;

import de.esoco.ewt.component.Chart;
import de.esoco.ewt.component.Chart.ChartType;
import de.esoco.ewt.style.StyleData;


/********************************************************************
 * A simple builder that creates {@link Chart} instances in a container builder.
 *
 * @author eso
 */
public class ChartBuilder
{
	//~ Constructors -----------------------------------------------------------

	/***************************************
	 * Private, only static use.
	 */
	private ChartBuilder()
	{
	}

	//~ Static methods ---------------------------------------------------------

	/***************************************
	 * Creates a new {@link Chart} component.
	 *
	 * @param  rBuilder   The builder to build the chart with
	 * @param  rStyleData The style data
	 * @param  eChartType The type of chart to display or NULL if it is set
	 *                    later through {@link Chart#setChartType(ChartType)}
	 *
	 * @return The new component
	 */
	public static Chart addChart(ContainerBuilder<?> rBuilder,
								 StyleData			 rStyleData,
								 ChartType			 eChartType)
	{
		Chart aChart = new Chart();

		rBuilder.addComponent(aChart, rStyleData, null, null);
		aChart.setChartType(eChartType);

		return aChart;
	}
}
