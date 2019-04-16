#!/usr/bin/env bash
mvn release:clean release:prepare -Prelease && mvn release:perform -Prelease && git pull --rebase