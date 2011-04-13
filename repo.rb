#! /usr/bin/jruby
# Simple console tool to manage SolverRepositary for OWLDSS Project
# It uses Java ObjectApi for this purpose and requires JRuby

if RUBY_PLATFORM !~ /java/
  puts "RepositoryManager requires JRuby to run, please get one"
  exit
end

require 'java'
PROJECT_ROOT = File.expand_path( Dir.pwd )
$CLASSPATH << File.join( PROJECT_ROOT, "bin" ) + '/'

Dir[File.join(PROJECT_ROOT, 'lib', '*/*.jar')].each { |jar| require jar }

java_import "core.repository.SolverRepository"
Repository = Java::CoreRepository::SolverRepository
Repo = Repository.new

require 'rubygems'
require 'manager/lib'
require 'commander/import'

global_option '--verbose'

program :name, 'OWLDSS Repository Console Manager'
program :version, '0.1'
program :description, 'Simple CLI tool to manage Solvers and their methods.'

command :solvers do |c|
  c.syntax = 'solvers [options]'
  c.option '--name STRING', String, 'Filters solvers by name'
  c.description = 'Shows list of available solvers'
  c.action do |args, options|
    solvers = ::Repo.get_solver_list_from_storage
    msg = "Following solvers are registered in repositary"
    if options.name
      msg << " (filter: *#{options.name}*)"
      solvers = solvers.reject{ |t| t !~ /#{options.name}/i }
    end
    puts msg
    puts ""
    if solvers.size == 0
      puts " NONE"
    else
      solvers.each_with_index { |t,i| puts sprintf " %2d) %s", i, t }
    end
    
  end
end


command :'solver describe' do |c|
  c.syntax = 'solver describe [options] [args]'
  c.description = 'Describes single solver'
  c.action do |args, options|
    puts "Searching by value *#{args.first}*"
    solvers = ::Repo.get_solver_list_from_storage
    solvers = solvers.reject{ |t| t !~ /#{args.first}/i }
    if solvers.size == 0
      puts "NONE"
    elsif solvers.size > 1
      puts "Ambiguity:"
      solvers.each_with_index { |t,i| puts sprintf " %2d) %s", i, t }
    else
    solverName = solvers.first
    solver_obj = Repo.get_solver solverName
    
    puts "SolverClass #{solverName}"
    puts ""
    puts "Mandatory parameters:"
    solver_obj.get_mandatory_params.each{ |t| puts t }
    puts ""
    puts "Options and default values:"
    solver_obj.get_options.each{ |k| puts "#{k[0]} = #{k[1]}" }
    end
  end
end

command :'solver add' do |c|
  c.syntax = 'solver add [options] [args]'
  c.description = 'Adds solver to repo'
  c.action do |args, options|
    puts "ClassName expected : #{args.first}"
  end
end

command :'solver remove' do |c|
  c.syntax = 'solver remove [options] [args]'
  c.description = 'Removes solver from repo'
  c.action do |args, options|
    puts "ClassName expected : #{args.first}"
  end
end

default_command :solvers
