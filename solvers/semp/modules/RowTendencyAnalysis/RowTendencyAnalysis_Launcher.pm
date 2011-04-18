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
uses RowTendencyAnalysis_Rules;
uses Global_XMLOutput;

//������� ������ ��� ������ ������
uses RowTendencyAnalysis_CreateData;

begin
	XMLOpenOutput();
	CallOnce( rule [CreateData] );
	CallOnce( rule [�����������_���������] );
	//CallOnce( rule [�������_������_���] );
	XMLCloseOutput();
end.
