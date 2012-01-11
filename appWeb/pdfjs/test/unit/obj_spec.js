/* -*- Mode: Java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */
/* vim: set shiftwidth=2 tabstop=2 autoindent cindent expandtab: */

'use strict';

describe("obj", function() {

  describe("Name", function() {
    it("should retain the given name", function() {
      var givenName = "Font";
      var name = new Name(givenName);
      expect(name.name).toEqual(givenName);
    });
  });
});

