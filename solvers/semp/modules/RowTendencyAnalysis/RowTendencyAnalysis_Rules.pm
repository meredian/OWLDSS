//==============================================================================
//  Задача EfficiencyTrendAnalysis
//  правила
//==============================================================================
uses CATNemNumbers, CATSempTypes, CATSempProductions, CATSempContainers;
uses Global_Ontology;
uses Global_Functions;
uses Global_XMLOutput;

//===================================================================================
rule Определение_тенденции
	forall row: TimeRow()
=>
	var am : real;
	var ten : int;
	var k  : int;
	var resultCode : int;
	var text : string;

	k:=#row.RowValue;
	ten := tendency(row.RowValue, min_el(row.RowValue), max_el(row.RowValue));

//	var presentationId : int := XMLGetNewId();
//	XMLOpenObject( "StringPresentation", presentationId );
//	XMLAddAttribute( "PresentationStringValue", "string", text );
//	XMLCloseObject();

	var tendencyId : int := XMLGetNewId();
	XMLOpenObject( "RowTendency", tendencyId );
	XMLAddObjectAttributeIRI( "HasRow", row.iri );
	XMLAddAttribute( "TendencyCode", "integer", ToString(ten) );
//	XMLAddObjectAttribute( "HasPresentation", presentationId );
	XMLCloseObject();

	XMLSetResultObject( tendencyId );
end;
//===================================================================================
