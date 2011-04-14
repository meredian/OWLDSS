module OWLDSS
  class MethodManipulator

    attr_accessor :solver_obj
    
    def initialize solver_obj
      @solver_obj = solver_obj
      @methods = solver_obj.get_methods
    end
    
    def print_methods
      puts "Methods:"
      @methods.each{ |t| puts t.get_name }
    end

  end
end