#!/bin/bash

set -e

sbt clean
rm -rf output-csv
rm -rf output-delta
