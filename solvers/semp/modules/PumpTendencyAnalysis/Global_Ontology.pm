//======================================================================
//  ��������� ���������� ������� "���������������"
//  �������� ������� � ���������
//======================================================================
uses CATNemNumbers, CATSempTypes, CATSempContainers;
//======================================================================

//�����-������ ���� ��������
class  BaseObject
   iri    : string;	//��� �������
   Id     : int;    //������������� �������
end;


//������ �������
class  Analysis_Object (BaseObject) 
end;


//�����-������ ��� ���� ��������� ��������
class Fault
   name : string;
end;

//������������������ ��������
type tuple_of__Object = tuple of BaseObject;


//���������� ������ �� �������������������� ���������� (���� ��� OAGA)
class OAGAReferenceData (BaseObject)
	MaxEfficiencyDeviation : real;  //����������� ���������� ���������� ��� �� ��������
end;

//����������-���������� ���������� � �������� ���������
class PumpReferenceData (BaseObject)
	typ  :	  int;	//2-������, 1-�����
	Qmin :	  real;	//����������� �����
	Qmax : 	  real;	//����������� �����

	H :	tuple of real;	//�����
	kz:	tuple of real;	//������������� �����
	Nmin :	real;	//������������ �������� ����
	Nmax :	real;	//������������ �������� ����

	kpdmin:	real;	//���
	kpdmax:	real;
	pmin :    real;	//��������
	pmax :    real;
	tmin :	real;	//�����������
	tmax :	real;
	no :		real;	//��������� �� ����� � � (�� �����)
end;

class TimeRow (BaseObject)
	RowValue :	tuple of real;
end;

class PumpEfficiencyDeviationRow (TimeRow)
//	RowValue :	tuple of real;
end;

class Pump (BaseObject)
	HasPumpReferenceData :  		PumpReferenceData;	//���������� ������ �� ������ �������� ���������
	HasPumpEfficiencyDeviationRow :	PumpEfficiencyDeviationRow;
end;

//������������� ������. ��������������� �����
class AnalysedObject
	Obj : BaseObject;
end;

class PumpEfficiencyTrend
	pump :		Pump;
	resultCode :	int;
	explanation:	string;  
end;

class RowTendency (BaseObject)
	HasRow :	TimeRow;
	TendencyCode :	int;
end;

class PumpEfficiencyWarning
	pump :		Pump;
	text:			string;  
end;

class PumpAnalysisResult
	pump :	Pump;
	trend :	PumpEfficiencyTrend;
	warn :	PumpEfficiencyWarning;
end;
