//==============================================================================
//  ‘ункции дл€ определени€ трендов
//==============================================================================
uses CATNemNumbers, CATSempTypes, CATSempProductions, CATSempContainers;

//===================================================================================

var eps : real:=0.001;
var porog : real:=0.005;

//===================================================================================
function min_el(vr: tuple of real) : real  //минимум временного р€да
begin
	var m : real;
	m :=vr[1];
	for  i in vr loop
		if (i<m)
		then m:=i;
		end;
    end;		
	return m;
end;
//===================================================================================
function max_el(vr: tuple of real) : real  //максимум временного р€да
begin
	var m : real;
	m :=vr[1];
	for  i in vr loop
		if (i>m)
		then m:=i;
		end;
    end;		
	return m;
end;
//===================================================================================
function near_l_limits(x, mn, mx, k : real) : boolean //проверка, подходит ли x близко к mn
begin
	if (x-mn >=0 & x-mn<k*(mx-mn))
	then return true;
	end;
	return false;
end;
//===================================================================================
function near_r_limits(x, mn, mx, k : real) : boolean //проверка, подходит ли x близко к mx
begin
	if (mx-x>=0 & mx-x<k*(mx-mn))
	then return true;
	end;
	return false;
end;
//===================================================================================
function arithmetic_mean(vr: tuple of real) : real // среднее арифметическое временного р€да
begin
	var am : real := 0.0;
	for  i in vr loop
		am:=am+i;
	end;	
	return am/#(vr);
end;
//===================================================================================
function difference(vr: tuple of real) : tuple of real // временной р€д разниц значений массива vr
begin
	var md : tuple of real:=real[];
	var n : int;
	n:=#(vr);
	for  i:=1 to n-1 step 1 loop
		md:=md+(vr[i+1]-vr[i]);
//		WriteLn(vr[i+1],"  ", vr[i]," -- ", vr[i+1]-vr[i], " -- ", md[i]);
	end;	
	return md;
end;
//===================================================================================
// „ерез сколько дней значени€ массива будут меньше среднего на reffd% при условии
// что начальное падение дебита было d и ежедневно оно увеличиваетс€ на pd
//===================================================================================
function num_day(vr: tuple of real, d, pd, reffd : real) : int
begin
	var nd: int := 0;
	var am, curdeb, nv : real;

    am:=arithmetic_mean(vr);
    curdeb := vr[1];
	while (am - curdeb <= 0.01*reffd*am) loop
		curdeb:=curdeb-d;
		vr:=vr+curdeb;
		d:=d+pd;
		am:=arithmetic_mean(vr);
		nd:=nd+1;
	end;
	return nd;
end;
//===================================================================================
// „ерез сколько часов (элементов) значени€ массива будут больше m на rd
//===================================================================================
function num_hour(vr: tuple of real, rd : real) : int
begin
	var dif: tuple of real;
	var nv : real;
	var nh, i : int;

	nh:=0;
	nv:=vr[1];
 	dif := difference(vr);
//	WriteLn(dif);
    i:=#dif;
//    WriteLn("nv=", nv, " m=", m, " rd=", rd);
    while (nv <= rd) loop
	 	nv:=nv-dif[i];
		i:=i-1;
		if i=0 then i:=#dif; end;
		nh:=nh+1;
	end;
	return nh;
end;//===================================================================================
//лингвистические значени€ функции tendency:
//1-монотонно падает, 2-колебл€сь падает, 4-стабильно, 5-колеблетс€ в пределах нормы,
//6-не имеет тренда, 10-колебл€сь растет, 11-монотонно растет
//===================================================================================
function tendency(vr: tuple of real, nsmax, nsmin : real) : int		
begin
	var cm, cb, cr : int ; // <, >, ==
	var cg : int; //граничные значени€
	var n : int;
	cm:=0; cb:=0; cr:=0; cg:=0;
	n:=#(vr);
	for i:=1 to n step 1 loop
		if (vr[i+1]-vr[i]>eps)
		then cm:=cm+1;
		end;
		if (vr[i]-vr[i+1]>eps)
		then cb:=cb+1;
		end;
		if ( Abs(vr[i]-vr[i+1])<=eps)
		then cr:=cr+1;
		end;
		if ( near_l_limits(vr[i],min_el(vr),max_el(vr), porog) |
		     near_r_limits(vr[i],min_el(vr),max_el(vr), porog) )
		then cg:=cg+1; //vr[i]приближаетс€ к границам
		end;
	end;
	if (cm=n-1 | (cb=0 & vr[1]<vr[n]))
	then return 1;
	end;
	if (cb=n-1 | (cm=0 & vr[1]>vr[n]))
	then return 11;
	end;
	if (cr=n-1)
	then return 4;
	end;
	if (cb<=n/4 & vr[1]<vr[n])
	then return 2;
	end;
	if (cm<=n/4 & vr[1]>vr[n])
	then return 10;
	end;
	if (cm!=0 & cb!=0 & cg=0)
	then return 5;
	end;
	return 6;
end;
//===================================================================================
function Round(r : real) : int
begin
   var c : int;

   c:=0;
   while Abs(r)>=c+1 loop
   		 c:=c+1;
   end;		
   return c;
end;
//===================================================================================
function Fract(r : real, n: int) : int    //дробна€ часть, n знаков после зпт.
begin
   var f : int;
   f:=Round((Abs(r)-Round(r))* Pow(10,n));
//   WriteLn("r-Round(r) = ",r-Round(r));
   return f;
end;
//===================================================================================
function RealToString(r : real) : string
begin
   var s : string;
   var c,d : int;

   c:= Round(r);
   d:= Fract(r,3);
   s:=ToString(c)+"."+ToString(d);
   return s;
end;
//===================================================================================
function IsPositive(vr: tuple of real) : boolean
begin
   	for  i in vr loop
		if (i<0)
		then return false;
		end;
    end;		
	return true;
end;
//===================================================================================
