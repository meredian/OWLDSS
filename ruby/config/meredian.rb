OWLDSS::RepoManipulator.set do

  solver("implementation.solvers.SempSolver") {

    method("EfficiencyTrendAnalysis"){
      param "MODULE_PATH", "~/.wine/drive_c/semp/modules/EfficiencyTrendAnalysis/"
      param "MODULE_LAUNCHER", "EfficiencyTrendAnalysis_Launcher.pm"
      param "MODULE_DATA_INPUT",
        "/home/meredian/.wine/drive_c/semp/modules/EfficiencyTrendAnalysis/EfficiencyTrendAnalysis_CreateData.pm"
      param "MODULE_CREATE_DATA_HEADER",
       "uses CATNemNumbers, CATSempTypes, CATSempProductions, CATSempContainers;\r\n" +
       "uses Global_Ontology;\r\n" +
       "rule CreateData\r\n" +
       "=>\r\n" +
       "new\r\n"
      param "MODULE_CREATE_DATA_TAIL","end;\r\n"
    }

    method("RowTendencyAnalysis"){
      param "MODULE_PATH", "~/.wine/drive_c/semp/modules/RowTendencyAnalysis/"
      param "MODULE_LAUNCHER", "RowTendencyAnalysis_Launcher.pm"
      param "MODULE_DATA_INPUT",
        "/home/meredian/.wine/drive_c/semp/modules/RowTendencyAnalysis/RowTendencyAnalysis_CreateData.pm"
      param "MODULE_CREATE_DATA_HEADER",
       "uses CATNemNumbers, CATSempTypes, CATSempProductions, CATSempContainers;\r\n" +
       "uses Global_Ontology;\r\n" +
       "rule CreateData\r\n" +
       "=>\r\n" +
       "new\r\n"
      param "MODULE_CREATE_DATA_TAIL","end;\r\n"
    }

    method("PumpTendencyAnalysis"){
      param "MODULE_PATH", "~/.wine/drive_c/semp/modules/PumpTendencyAnalysis/"
      param "MODULE_LAUNCHER", "PumpTendencyAnalysis_Launcher.pm"
      param "MODULE_DATA_INPUT",
        "/home/meredian/.wine/drive_c/semp/modules/PumpTendencyAnalysis/PumpTendencyAnalysis_CreateData.pm"
      param "MODULE_CREATE_DATA_HEADER",
        "uses CATNemNumbers, CATSempTypes, CATSempProductions, CATSempContainers;\r\n" +
        "uses Global_Ontology;\r\n" +
       "rule CreateData\r\n" +
       "=>\r\n" +
       "new\r\n"
      param "MODULE_CREATE_DATA_TAIL","end;\r\n"
    }

  }

  solver("implementation.solvers.PumpAnalysisComplexSolver") {
    method("Standard"){
    }
  }

  solver("implementation.solvers.RowTendencySimpleAnalyser") {
    method("Standard"){
    }
  }

end
