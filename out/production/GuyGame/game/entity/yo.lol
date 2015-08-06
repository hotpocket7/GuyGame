
private void handleBlockCollisions(char axis) {
        if (axis != 'x' && axis != 'y') {
        System.err.println("Invalid axis!");
        return;
        }

        boolean onGround = false;
        ArrayList<Block> specialBlocks = new ArrayList<>();

        for (Block block : Level.getCurrentLevel().blocks) {
        if(!block.active || !block.isCollidable() || !block.isOnScreen()) continue;


        RectangularHitbox blockHitbox = block.getHitbox();
        PolygonHitbox blockHitbox2 = block.getPolygonHitbox();

        if (!hitbox.collides(block.getHitbox())) {
        if(block.isCollidable() && !block.hasSpecialHitbox() && block.solidTop && axis == 'y') {
        if(block.getHitbox().bounds.contains(position.x, position.y + height + 1)
        || block.getHitbox().bounds.contains(position.x + width, position.y + height + 1)) {
        onGround = true;
        position.plusEquals(block.velocity);
        }
        if(block.getHitbox().bounds.contains(position.x, position.y + height + 1)
        && block.getHitbox().bounds.contains(position.x + width, position.y + height + 1)) {
        block.collide(this);
        }
        }
        continue;
        }

        if (!block.solid) {
        block.collide(this);
        }

        Rectangle2D intersection = hitbox.bounds.createIntersection(blockHitbox.bounds);
        double w = intersection.getWidth();
        double h = intersection.getHeight();
        if(block.solid) {
        switch (axis) {
        case 'x':
        if(block.hasSpecialHitbox()) {
        specialBlocks.add(block);
        break;
        }
        if (w >= h || block.position.y > position.y - velocity.y + height)
        break; //No horizontal collision
        if (position.x < blockHitbox.position.x && block.solidLeft) {
        //Collision to right of player
        position.x = blockHitbox.position.x - width;
        } else if (position.x > blockHitbox.position.x && block.solidRight) {
        //Collision to left of player
        position.x = blockHitbox.position.x + blockHitbox.width;
        }
        block.collide(this);
        break;
        case 'y':
        if (position.y > blockHitbox.position.y && block.solidBottom) {
        //Collision above player
        if(block.hasSpecialHitbox()) {
        specialBlocks.add(block);
        break;
        }
        position.y = blockHitbox.position.y + blockHitbox.height;
        velocity.y = 0;
        block.collide(this);
        } else if (position.y <= blockHitbox.position.y && block.solidTop) {
        //Collision below player
        if(!block.solidBottom
        && position.y + height - velocity.y - acceleration.y >
        blockHitbox.position.y - block.velocity.y)
        break;
        if (block.hasSpecialHitbox()) {
        specialBlocks.add(block);
        break;
        }
        position.y = blockHitbox.position.y - height;
        velocity.y = 0;
        state = State.GROUNDED;
        onGround = true;
        block.collide(this);
        }
        break;
        }
        }
        }
        updateBounds();
        if(axis == 'y') {
        if (!onGround) {
        if (state == State.GROUNDED)
        jumpsUsed++;
        state = State.AIRBORNE;
        }
        for(Block block : specialBlocks) {
        if (hitbox.collides(block.getPolygonHitbox())) {
        block.collide(this);
        }
        }
        specialBlocks.clear();
        }
        }