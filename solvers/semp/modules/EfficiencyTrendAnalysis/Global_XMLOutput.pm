//======================================================================
// Methods for XML-like object presentations
//======================================================================
uses CATNemNumbers, CATSempTypes, CATSempProductions, CATSempContainers;
//======================================================================

var newId : int := 0;
var resultObject : int := -1;
var resultObjectIRI : string := "";

procedure XMLOpenOutput()
begin
	WriteLn("<individuals>");
end;

function XMLGetNewId(): int
begin
	newId := newId + 1;
	return newId;
end;

procedure XMLOpenObject( className : string, id : int )
begin
	WriteLn("<individual class='",className,"' id='",id,"'>");
end;

procedure XMLOpenObjectWithIRI( className : string, id : int, iri : string )
begin
	WriteLn("<individual class='",className,"' id='",id,"' iri='",iri,"'>");
end;

procedure XMLAddAttribute( name, typ, value : string )
begin
	WriteLn("<attr type='", typ, "' name='", name, "' value='", value, "'/>");
end;

procedure XMLAddObjectAttributeIRI( name : string, iri : string )
begin
	WriteLn("<attr type='object' name='", name, "' iri='", iri, "'/>");
end;

procedure XMLAddObjectAttribute( name : string, id : int )
begin
	WriteLn("<attr type='object' name='", name, "' id='", id, "'/>");
end;

procedure XMLSetResultObject( id : int )
begin
	resultObject := id;
end;

procedure XMLSetResultObjectIRI( iri : string )
begin
	resultObjectIRI := iri;
end;

procedure XMLCloseObject()
begin
	WriteLn("</individual>");
end;

procedure XMLCloseOutput()
begin
	if (resultObject >= 0) then
		WriteLn("<result id='",resultObject,"'/>");
	else
		if (resultObjectIRI != "") then
			WriteLn("<result iri='",resultObjectIRI,"'/>");
		end;
	end;
	WriteLn("</individuals>");
end;
