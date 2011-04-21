//======================================================================
//  Онтология предметной области "Нефтегазадобыча"
//  Описание классов и отношений
//======================================================================
uses CATNemNumbers, CATSempTypes, CATSempContainers;
//======================================================================

//Класс-предок всех объектов
class  BaseObject
   iri    : string;	//Имя объекта
   Id     : int;    //Идентификатор объекта
end;


//Объект анализа
class  Analysis_Object (BaseObject) 
end;


//Класс-предок для всех неполадок объектов
class Fault
   name : string;
end;

//Последовательность объектов
type tuple_of__Object = tuple of BaseObject;


//Справочные данные по нефтегазодобывающему управлению (НГДУ или OAGA)
class OAGAReferenceData (BaseObject)
	MaxEfficiencyDeviation : real;  //Максимально допустимое отклонение кпд от номинала
end;

//Нормативно-справочная информация о насосной установке
class PumpReferenceData (BaseObject)
	typ  :	  int;	//2-нагнет, 1-добыв
	Qmin :	  real;	//Минимальный дебит
	Qmax : 	  real;	//Минимальный дебит

	H :	tuple of real;	//Напор
	kz:	tuple of real;	//Кавитационный запас
	Nmin :	real;	//Потребляемая мощность кВтч
	Nmax :	real;	//Потребляемая мощность кВтч

	kpdmin:	real;	//КПД
	kpdmax:	real;
	pmin :    real;	//Давление
	pmax :    real;
	tmin :	real;	//Температура
	tmax :	real;
	no :		real;	//Наработка на отказ в ч (не менее)
end;

class TimeRow (BaseObject)
	RowValue :	tuple of real;
end;

class PumpEfficiencyDeviationRow (TimeRow)
//	RowValue :	tuple of real;
end;

class Pump (BaseObject)
	HasPumpReferenceData :  		PumpReferenceData;	//Справочные данные по данной насосной установке
	HasPumpEfficiencyDeviationRow :	PumpEfficiencyDeviationRow;
end;

//Анализируемый объект. Вспомогательный класс
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
