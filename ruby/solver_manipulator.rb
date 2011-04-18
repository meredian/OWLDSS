module OWLDSS
  class SolverManipulator
  
    attr_accessor :repo
    
    def initialize repo
      @repo = repo
      @solvers = repo.get_solver_list_from_storage
    end
    
    def list_solvers filter
      solvers = @solvers.reject { |t| t !~ /#{filter}/i }
      print_solvers solvers, filter
    end
    
    def print_solvers solvers, filter
      msg = "Solvers registered"
      msg << " (filter: *#{filter}*)" if filter
      msg << ' :'
      puts msg
      if !solvers || solvers.empty?
        puts "NONE"
      else
        solvers.each_with_index { | t,i| puts "#{i}) #{t}" } 
      end
    end
    
    def update_repo
      repo.save_to_storage
      update_solvers
    end
    
    def update_solvers
      @solvers = repo.get_solver_list_from_storage
    end
    
    def get_solver name
      repo.get_solver name
    end
    
    def describe_solver solver
      solver_obj = get_solver solver
      puts "SolverClass \"#{solver}\""
      puts ""
      print_mandatory solver_obj
      puts ""
      print_options solver_obj
      puts ""
      manipulator = MethodManipulator.new solver_obj
      manipulator.print_methods
    end
    
    def print_mandatory solver_obj
      puts "Mandatory parameters:"
      solver_obj.get_mandatory_params.each{ |t| puts t }
    end
    
    def print_options solver_obj
      puts "Options and default values:"
      solver_obj.get_options.each{ |k| puts "#{k[0]} = #{k[1]}" }
    end
    
    def select_solver filter
      solvers = @solvers.reject { |t| t !~ /#{filter}/i }
      if !solvers || solvers.size != 1
        puts "Ambiguity :"
        print_solvers solvers, filter
        return nil
      else
        return solvers[0]
      end 
    end
    
  end
end