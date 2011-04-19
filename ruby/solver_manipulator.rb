module OWLDSS
  class SolverManipulator

    attr_accessor :solver

    def initialize solver
      @solver = solver
      @methods = solver.get_methods
    end

    def print_methods
      puts "Methods:"
      @methods.each{ |t| puts t.get_name }
    end

    def method method_name, &block
      manipulator = MethodManipulator.new Java::CoreRepository::MethodSignature.new method_name
      manipulator.instance_eval &block if block_given?
      @solver.addMethod manipulator.method
    end
  end
end