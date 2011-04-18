//==============================================================================
// Задача определения падения кпд насосов
// EfficiencyTrendAnalysis
//==============================================================================
// Инструментальные модули
uses CATNemNumbers, CATSempContainers, CATSempTypes, CATSempProductions;
uses CATNemStrings;

// правила из области ППР на НГДП
uses Global_Ontology;
uses Global_Functions;
uses EfficiencyTrendAnalysis_Rules;
uses Global_XMLOutput;

//входные данные для данной задачи
uses EfficiencyTrendAnalysis_CreateData;

begin
	XMLOpenOutput();
	CallOnce( rule [CreateData] );
	CallOnce( rule [Выявление_тренда_кпд] );
	//CallOnce( rule [Прогноз_тренда_кпд] );
	XMLCloseOutput();
end.
