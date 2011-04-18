//==============================================================================
//  Задача EfficiencyTrendAnalysis
//  правила
//==============================================================================
uses CATNemNumbers, CATSempTypes, CATSempProductions, CATSempContainers;
uses Global_Ontology;
uses Global_Functions;
uses Global_XMLOutput;

//===================================================================================
rule Выявление_тренда_кпд
	forall pu: Pump(HasPumpEfficiencyDeviationRow : row); oagaRefData: OAGAReferenceData(); row: PumpEfficiencyDeviationRow()
=>
	var am : real;
	var ten : int;
	var k  : int;
	var resultCode : int;
	var text : string;

	k:=#row.RowValue;
	ten := tendency(row.RowValue, min_el(row.RowValue), max_el(row.RowValue));

	var warnId : int := -1;

	if ( row.RowValue[1] - 0 ) > 0.01*oagaRefData.MaxEfficiencyDeviation
	then
		resultCode := 2;
		text := "У насоса зафиксировано падение КПД сверх норматива.";
	else if ten>=10
		then
			resultCode := 1;
			text := "У насоса зафиксировано падение КПД в пределах нормы.";

			var nh : int;
			var m  : int;
			
			m:=#row.RowValue;
			nh:=num_hour(row.RowValue, 0.01*oagaRefData.MaxEfficiencyDeviation);

			if nh<=24 then
				warnId := XMLGetNewId();
				XMLOpenObject( "PumpEfficiencyWarning", warnId );
				XMLAddObjectAttributeIRI( "HasWarnedPump", pu.iri );
				XMLAddAttribute( "WarningExplanation", "string", "КПД насоса упадет ниже норматива через " + ToString(nh) + " часов" );
			XMLCloseObject();
		end;
  		else
			resultCode := 0;
			text := "У насоса не зафиксировано падение КПД.";
  		end;
	end;

	var presentationId : int := XMLGetNewId();
	XMLOpenObject( "StringPresentation", presentationId );
	XMLAddAttribute( "PresentationStringValue", "string", text );
	XMLCloseObject();

	var trendId : int := XMLGetNewId();
	XMLOpenObject( "PumpEfficiencyTrend", trendId );
	XMLAddObjectAttributeIRI( "HasTrendPump", pu.iri );
	XMLAddObjectAttribute( "HasPresentation", presentationId );
	if warnId >= 0 then
		XMLAddObjectAttribute( "HasWarning", warnId );
	end;
	XMLAddAttribute( "TrendCode", "int", ToString(resultCode) );
	XMLAddAttribute( "TrendExplanation", "string", text );
	XMLCloseObject();

	XMLSetResultObject( trendId );
end;
//===================================================================================
