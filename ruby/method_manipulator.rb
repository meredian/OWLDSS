module OWLDSS
  class MethodManipulator

    attr_accessor :method

    def initialize method
      @method = method
    end

    def param name, value
      @method.set_param name.to_s, value.to_s
    end

    def option name, value
      @method.setOption name.to_s, value.to_s
    end
  end
end