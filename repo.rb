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

require 'pp'
require 'rubygems'
require 'commander/import'
require 'ruby/repo_manipulator'
require 'ruby/solver_manipulator'
require 'ruby/method_manipulator'

global_option '--verbose', "Verbose information about all performed operations"

program :name, 'OWLDSS Repository Console Manager'
program :version, '0.1'
program :description, 'Simple CLI tool to manage Solvers and their methods.'

command :init do |c|
  c.syntax = 'init [options]'
  c.description = 'Inits repository with preconfigured settings'
  c.option '--with-config STRING', String, 'Selects which config should be applied'
  c.action do |args, options|
    require "ruby/config/#{options.with_config.to_s}"
  end
end

command :solvers do |c|
  c.syntax = 'solvers [options]'
  c.description = 'Shows list of available solvers, uses args[0] as name-filter'
  c.action do |args, options|
    filter = args[0]
    manipulator = ::OWLDSS::RepoManipulator.new ::Repo
    manipulator.list_solvers filter
  end
end


command :'solver describe' do |c|
  c.syntax = 'solver describe [options] [args]'
  c.description = 'Describes single solver'
  c.action do |args, options|
    filter = args[0]
    manipulator = ::OWLDSS::RepoManipulator.new ::Repo
    solver = manipulator.select_solver filter
    manipulator.describe_solver solver if solver
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
