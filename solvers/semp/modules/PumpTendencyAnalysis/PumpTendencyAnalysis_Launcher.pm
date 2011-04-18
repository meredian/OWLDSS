//==============================================================================
// ������ ����������� ������� ��� �������
// EfficiencyTrendAnalysis
//==============================================================================
// ���������������� ������
uses CATNemNumbers, CATSempContainers, CATSempTypes, CATSempProductions;
uses CATNemStrings;

// ������� �� ������� ��� �� ����
uses Global_Ontology;
uses Global_Functions;
uses PumpTendencyAnalysis_Rules;
uses Global_XMLOutput;

//������� ������ ��� ������ ������
uses PumpTendencyAnalysis_CreateData;

begin
	XMLOpenOutput();
	CallOnce( rule [CreateData] );
	CallOnce( rule [���������_������_���] );
	//CallOnce( rule [�������_������_���] );
	XMLCloseOutput();
end.
