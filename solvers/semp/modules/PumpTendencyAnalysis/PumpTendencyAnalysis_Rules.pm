//==============================================================================
//  ������ EfficiencyTrendAnalysis
//  �������
//==============================================================================
uses CATNemNumbers, CATSempTypes, CATSempProductions, CATSempContainers;
uses Global_Ontology;
uses Global_Functions;
uses Global_XMLOutput;

//===================================================================================
rule ���������_������_���
	forall pu: Pump(); oagaRefData: OAGAReferenceData(); rowTendency: RowTendency(HasRow: row); row: TimeRow()
=>
	var am : real;
	var k  : int;
	var resultCode : int;
	var text : string;

	k:=#row.RowValue;

	var warnId : int := -1;

	if ( row.RowValue[1] - 0 ) > 0.01*oagaRefData.MaxEfficiencyDeviation
	then
		resultCode := 2;
		text := "� ������ ������������� ������� ��� ����� ���������.";
	else if rowTendency.TendencyCode>=10
		then
			resultCode := 1;
			text := "� ������ ������������� ������� ��� � �������� �����.";

			var nh : int;
			var m  : int;
			
			m:=#row.RowValue;
			nh:=num_hour(row.RowValue, 0.01*oagaRefData.MaxEfficiencyDeviation);

			if nh<=24 then
				warnId := XMLGetNewId();
				XMLOpenObject( "PumpEfficiencyWarning", warnId );
				XMLAddObjectAttributeIRI( "HasWarnedPump", pu.iri );
				XMLAddAttribute( "WarningExplanation", "string", "��� ������ ������ ���� ��������� ����� " + ToString(nh) + " �����" );
			XMLCloseObject();
		end;
  		else
			resultCode := 0;
			text := "� ������ �� ������������� ������� ���.";
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
